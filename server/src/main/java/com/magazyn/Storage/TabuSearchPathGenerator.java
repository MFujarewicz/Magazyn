package com.magazyn.Storage;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.magazyn.JobType;
import com.magazyn.Map.Map;
import com.magazyn.database.Product;

public class TabuSearchPathGenerator implements IPathGenerator {
    private Map map;
    private double max_weight;
    private HashMap<Integer, AbstractMap.SimpleEntry<Product, JobType>> products;

    @Override
    public List<AbstractMap.SimpleEntry<Product, JobType>> generatePath(List<AbstractMap.SimpleEntry<Product, JobType>> products, Map map, double max_weight) {

        if (products.size() == 0) {
            return products;
        }

        if (products.size() <= 6) {
            return generatePathBruteForce(products, map, max_weight);
        }

        this.map = map;
        this.max_weight = max_weight;
        this.products = new HashMap<Integer, AbstractMap.SimpleEntry<Product, JobType>>();
        for (var elem : products) {
            this.products.put(elem.getKey().getID(), elem);
        }
        
        return TabuSearch(products);
    }

    private List<AbstractMap.SimpleEntry<Product, JobType>> generatePathBruteForce(List<AbstractMap.SimpleEntry<Product, JobType>> products, Map map, double max_weight)
    {
        List<AbstractMap.SimpleEntry<Product, JobType>> products_premutation = products.stream().sorted((x, y) ->  x.getKey().getID().compareTo(y.getKey().getID()))
            .collect(Collectors.toList());

        double min_distance = calculateDistance(products_premutation, map);
        var best_products_list = new ArrayList<AbstractMap.SimpleEntry<Product, JobType>>(products_premutation);

        while (nextPermutation(products_premutation)) {
            if (isPathValid(products_premutation, max_weight)) {
                double new_distance = calculateDistance(products_premutation, map);

                if (new_distance < min_distance) {
                    min_distance = new_distance;
                    best_products_list = new ArrayList<AbstractMap.SimpleEntry<Product, JobType>>(products_premutation);
                }
            }
        }
        
        return best_products_list;
    }

    private double calculateDistance(List<AbstractMap.SimpleEntry<Product, JobType>> products, Map map) {
        double distance = 0.0;

        Product begin = products.get(0).getKey();
        Product end = products.get(products.size() - 1).getKey();

        distance += map.getDistance(begin.getProductLocation().getID_rack(), begin.getProductLocation().getRack_placement(), Map.MainPoint.IN);
        distance += map.getDistance(end.getProductLocation().getID_rack(), end.getProductLocation().getRack_placement(), Map.MainPoint.OUT);

        for (int i = 1; i < products.size(); i++)
        {
            Product last = products.get(i - 1).getKey();
            Product next = products.get(i).getKey();
            distance += map.getDistance(last.getProductLocation().getID_rack(), last.getProductLocation().getRack_placement(), next.getProductLocation().getID_rack(), last.getProductLocation().getRack_placement());
        }

        return distance;
    }

    private boolean isPathValid(List<AbstractMap.SimpleEntry<Product, JobType>> products, double max_weight) {
        double current_weight = 0.0;

        for (var elem : products) {
            if (elem.getValue() == JobType.take_in) {
                current_weight += elem.getKey().getProductData().getWeight();
            }
        }

        double eps = 0.000001;

        for (var elem : products) {
            if (elem.getValue() == JobType.take_in) {
                current_weight -= elem.getKey().getProductData().getWeight();
            }
            else {
                current_weight += elem.getKey().getProductData().getWeight();
            }

            if (current_weight + eps > max_weight) {
                return false;
            }
        }

        return true;
    }

    public boolean nextPermutation(List<AbstractMap.SimpleEntry<Product, JobType>> products) {
        int i = products.size() - 2;

        for (; i >= 0; i--) {
            AbstractMap.SimpleEntry<Product, JobType> next = products.get(i + 1);
            AbstractMap.SimpleEntry<Product, JobType> curr = products.get(i);
            if (curr.getKey().getID().compareTo(next.getKey().getID()) < 0) {
                break;
            }
        }

        if (i < 0) {
            return false;
        }

        AbstractMap.SimpleEntry<Product, JobType> pivot = products.get(i);
        int next = products.size() - 1;

        for (int j = products.size() - 1; j > i; j--) {
            if (pivot.getKey().getID().compareTo(products.get(j).getKey().getID()) < 0) {
                next = j;
                break;
            }
        }

        Collections.swap(products, i, next);
        reverse(products, i + 1, products.size() - 1);

        return true;
    }

    static <T> void reverse(List<T> list, int left, int right) 
    { 
        while (left < right) { 
            var temp = list.get(left); 
            list.set(left++, list.get(right));
            list.set(right--, temp);
        } 
    } 

    //Tabo search
    //--------------------------------------------------------------------------------------------------------------------------------------------------------------------//
    private class xoshiro256pRND
    {
        private long state[] = new long[4];

        public xoshiro256pRND() {
            var rand = new Random();
            state[0] = rand.nextInt();
            state[0] <<= 32;
            state[0] |= rand.nextInt();
            state[1] = rand.nextInt();
            state[1] <<= 32;
            state[1] |= rand.nextInt();
            state[2] = rand.nextInt();
            state[2] <<= 32;
            state[2] |= rand.nextInt();
            state[3] = rand.nextInt();
            state[3] <<= 32;
            state[3] |= rand.nextInt();
        }

        long next(long begin, long end)
        {
            if ((end - begin) == 0) return 0;

            long result = state[0] + state[3];
            long t = state[1] << 17;

            state[2] ^= state[0];
            state[3] ^= state[1];
            state[1] ^= state[2];
            state[0] ^= state[3];

            state[2] ^= t;
            state[3] = (state[3] << 45) | (state[3] >>> (64 - 45));

            return Math.abs((result % (end - begin)) + begin);
        }
    }

    class TaboList
    {
        final public long max_tabo_size;

        public TaboList(long max_tabo_size) {
            this.max_tabo_size = max_tabo_size;
        }

        public HashSet<Pair<Integer, Integer>> set = new HashSet<Pair<Integer, Integer>>();
        public Queue<Pair<Integer, Integer>> queue = new LinkedList<Pair<Integer, Integer>>();
    }

    private class Pair<T, U> implements Comparable<T> {
        public T first;
        public U second;

        public Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public int hashCode() {
            return first.hashCode() ^ second.hashCode();
        }

        @Override 
        @SuppressWarnings("all")
        public boolean equals(Object other) {
            if (!(other instanceof Pair)) {
                return false;
            }

            Pair<T, U> pair = (Pair<T, U>)other;

            return pair.first.equals(first) && pair.second.equals(second);
        }

        @Override
        @SuppressWarnings("all")
        public int compareTo(Object other) {
            if (!(other instanceof Pair)) {
                return 0;
            }

            var first_c = (Comparable) first;
            var first_o_c = (Comparable)((Pair<T, U>)other).first;

            return first_c.compareTo(first_o_c);
        }
    }

    private double calculateDistance(List<Integer> path) {
        double distance = 0.0;

        Product begin = products.get(path.get(0)).getKey();
        Product end = products.get((path.get(path.size() - 1))).getKey();

        distance += map.getDistance(begin.getProductLocation().getID_rack(), begin.getProductLocation().getRack_placement(), Map.MainPoint.IN);
        distance += map.getDistance(end.getProductLocation().getID_rack(), end.getProductLocation().getRack_placement(), Map.MainPoint.OUT);

        for (int i = 1; i < products.size(); i++)
        {
            Product last = products.get(path.get(i - 1)).getKey();
            Product next = products.get(path.get(i)).getKey();
            distance += map.getDistance(last.getProductLocation().getID_rack(), last.getProductLocation().getRack_placement(), next.getProductLocation().getID_rack(), last.getProductLocation().getRack_placement());
        }

        return distance;
    }

    private boolean isPathValid(List<Integer> path) {
        double current_weight = 0.0;

        for (var elem : path) {
            if (products.get(elem).getValue() == JobType.take_in) {
                current_weight += products.get(elem).getKey().getProductData().getWeight();
            }
        }

        double eps = max_weight * 0.01;

        for (var elem : path) {
            if (products.get(elem).getValue() == JobType.take_in) {
                current_weight -= products.get(elem).getKey().getProductData().getWeight();
            }
            else {
                current_weight += products.get(elem).getKey().getProductData().getWeight();
            }

            if (current_weight - eps > max_weight) {
                return false;
            }
        }

        return true;
    }

    ArrayList<ArrayList<Integer>> gen2_5Opt(ArrayList<Integer> path, Pair<Integer, Integer> to_cut, TaboList tabo_list)
    {
        var x = to_cut.first;
        var y = to_cut.second;
        /*if (x > y)
            std::swap(x, y);
        if (x > path.size() - 3)
        {
            x = path.size() - 3;
        }
        if (x == y)
            y++;
        if (x + 1 == y)
            y++;
        if (x == 0 && y == path.size() - 1)
            x++;*/

        
        ArrayList<Integer> path_tmp = new ArrayList<Integer>(path);
        ArrayList<ArrayList<Integer>> ret = new ArrayList<ArrayList<Integer>>();
        boolean a = false, b = false, c = false;

        //1
        if (tabo_list.set.contains(new Pair<>(path.get(x), path.get(y))) == false)
        {
            if (tabo_list.set.contains(new Pair<>(path.get(x), path.get(y - 1))) == false)
            {
                if (tabo_list.set.contains(new Pair<>(path.get(x + 2), path.get(x + 1))) == false)
                {
                    if (tabo_list.set.contains(new Pair<>(path.get(x + 1), path.get((y + 1) % path.size()))) == false)
                    {
                        Collections.reverse(path_tmp.subList(1 + x, 1 + y));
                        ret.add(path_tmp);

                        a = true;
                    }
                }
            }
        }

        
        //2
        path_tmp = new ArrayList<>(path);
        if (tabo_list.set.contains(new Pair<>(path.get(x), path.get(x + 2))) == false)
        {
            if (tabo_list.set.contains(new Pair<>(path.get(y), path.get(y - 1))) == false)
            {
                if (tabo_list.set.contains(new Pair<>(path.get(y), path.get(x + 1))) == false)
                {
                    if (tabo_list.set.contains(new Pair<>(path.get(x + 1), path.get((y + 1) % path.size()))) == false)
                    {          
                        Collections.rotate(path_tmp.subList(1 + x, y), 1);
                        ret.add(path_tmp);

                        b = true;
                    }
                }
            }
        }

        //3
        path_tmp = new ArrayList<>(path);

        if (tabo_list.set.contains(new Pair<>(path.get(x), path.get(y))) == false)
        {
            if (tabo_list.set.contains(new Pair<>(path.get(y), path.get(x + 1))) == false)
            {
                if (tabo_list.set.contains(new Pair<>(path.get(x + 2), path.get(x + 1))) == false)
                {
                    if (tabo_list.set.contains(new Pair<>(path.get(y - 1), path.get((y + 1) % path.size()))) == false)
                    {
                        Collections.rotate(path_tmp.subList(1 + x, y), -1);
                        ret.add(path_tmp);

                        c = true;
                    }
                }
            }
        }

        if (b)
        {
            tabo_list.set.add(new Pair<>(path.get(x), path.get(x + 2)));
            tabo_list.queue.add(new Pair<>(path.get(x), path.get(x + 2)));
        }

        if (c)
        {
            tabo_list.set.add(new Pair<>(path.get(y - 1), path.get((y + 1) % path.size())));
            tabo_list.queue.add(new Pair<>(path.get(y - 1), path.get((y + 1) % path.size())));
        }

        if (a || c)
        {
            tabo_list.set.add(new Pair<>(path.get(x + 2), path.get(x + 1)));
            tabo_list.queue.add(new Pair<>(path.get(x + 2), path.get(x + 1)));

            tabo_list.set.add(new Pair<>(path.get(x), path.get(y)));
            tabo_list.queue.add(new Pair<>(path.get(x), path.get(y)));
        }

        if (a || b)
        {
            tabo_list.set.add(new Pair<>(path.get(y), path.get(y - 1)));
            tabo_list.queue.add(new Pair<>(path.get(y), path.get(y - 1)));

            tabo_list.set.add(new Pair<>(path.get(x + 1), path.get((y + 1) % path.size())));
            tabo_list.queue.add(new Pair<>(path.get(x + 1), path.get((y + 1) % path.size())));
        }

        if (b || c)
        {
            tabo_list.set.add(new Pair<>(path.get(y), path.get(x + 1)));
            tabo_list.queue.add(new Pair<>(path.get(y), path.get(x + 1)));
        }

        while (tabo_list.queue.size() > tabo_list.max_tabo_size)
        {
            var elem = tabo_list.queue.poll();
            tabo_list.set.remove(elem);
        }

        return ret;
    }

    private List<AbstractMap.SimpleEntry<Product, JobType>> TabuSearch(List<AbstractMap.SimpleEntry<Product, JobType>> products_list) {
        ArrayList<Integer> path = new ArrayList<Integer>();
        for (AbstractMap.SimpleEntry<Product, JobType> elem : products_list) {
            path.add(elem.getKey().getID());
        }
        
        TaboList tabo_list = new TaboList((long)(4.7 * products_list.size()));

        TreeMap<Pair<Double, ArrayList<Integer>>, Pair<Boolean, Long>> best_results = new TreeMap<Pair<Double, ArrayList<Integer>>, Pair<Boolean, Long>>();
        long best_results_max_size = products_list.size() * products_list.size();

        HashSet<Pair<Integer, Integer>> combinations = new HashSet<Pair<Integer, Integer>>();

        Pair<ArrayList<Integer>, Double> curr = new Pair<ArrayList<Integer>, Double>(path,  calculateDistance(path));
        Pair<ArrayList<Integer>, Double> new_curr = new Pair<ArrayList<Integer>, Double>(path,  calculateDistance(path));

        best_results.put(new Pair<Double, ArrayList<Integer>>(curr.second, curr.first), new Pair<Boolean, Long>(false, 0L));

        LinkedList<Pair<Double, ArrayList<Integer>>> curr_list = new LinkedList<Pair<Double, ArrayList<Integer>>>();

        //Tabu search

        long k = 0;
        long l = 0;
        long counter = 0;

        long start = System.currentTimeMillis();
        long time = 3 /*s*/ * 1000 /*ms*/;

        var random = new xoshiro256pRND();

        while ((System.currentTimeMillis() - start) < time)
        {
            int a, b;
            a = (int)random.next(0, path.size() - 3);
            b = (int)random.next(2, path.size() - 1);

            if (a > b) {
                var tmp = a;
                a = b;
                b = tmp;
            }
            if (a > products_list.size() - 3)
            {
                a = products_list.size() - 3;
            }
            if (a == b)
                b++;
            if (a + 1 == b)
                b++;
            if (a == 0 && b == products_list.size() - 1)
                b++;

            if (combinations.contains(new Pair<Integer, Integer>(a, b)))
            {
                k++;

                if (k > 5)
                {
                    var elem = best_results.get(new Pair<Double, ArrayList<Integer>>(curr.second, curr.first));
                    if (elem != null)
                    {
                        elem.first = true;
                        elem.second = counter + 100000000;
                    }

                    if (curr_list.size() != 0)
                    {
                        var type = curr_list.get(0);

                        curr.second = type.first;
                        curr.first = type.second;
                        new_curr.second = type.first;
                        new_curr.first = type.second;

                        k = 0;
                        l++;
                        curr_list.clear();
                        combinations.clear();
                        path = type.second;
                    }
                    else
                    {
                        for (int i = 0; i < 50; i++) {
                            ArrayList<Integer> new_path = new ArrayList<Integer>();
                            for (AbstractMap.SimpleEntry<Product, JobType> value : products_list) {
                                new_path.add(value.getKey().getID());
                            }

                            Collections.shuffle(new_path, new Random());

                            if (!isPathValid(new_path))
                                continue;

                            curr.second = calculateDistance(new_path);
                            curr.first = new_path;
                            new_curr.second = curr.second;
                            new_curr.first = new_path;

                            if (curr.second < best_results.firstKey().first)
                            {
                                best_results.put(new Pair<>(curr.second, curr.first), new Pair<Boolean, Long>(false, 0L));
                            }

                            k = 0;
                            curr_list.clear();
                            combinations.clear();
                            path = new_path;
                            break;
                        }

                        l++;
                    }


                    if (l > 100)
                    {
                        var it = best_results.entrySet().iterator();
                        boolean init = false;
                        while (it.hasNext())
                        {
                            var value = it.next();
                            if (!value.getValue().first)
                            {
                                curr.second = best_results.firstKey().first;
                                curr.first = best_results.firstKey().second;
                                new_curr.second = best_results.firstKey().first;
                                new_curr.first = best_results.firstKey().second;
                                init = true;
                                path = curr.first;

                                break;
                            }
                            else
                            {
                                if (value.getValue().second < counter)
                                {
                                    value.getValue().first = false;

                                    curr.second = best_results.firstKey().first;
                                    curr.first = best_results.firstKey().second;
                                    new_curr.second = best_results.firstKey().first;
                                    new_curr.first = best_results.firstKey().second;
                                    init = true;
                                    path = curr.first;

                                    break;
                                }
                            }
                        }

                        if (!init)
                        {
                            ArrayList<Integer> new_path = new ArrayList<Integer>();
                            for (AbstractMap.SimpleEntry<Product, JobType> value : products_list) {
                                new_path.add(value.getKey().getID());
                            }

                            curr.second = calculateDistance(new_path);
                            curr.first = new_path;
                            new_curr.second = curr.second;
                            new_curr.first = new_path;
                            path = curr.first;

                            if (curr.second < best_results.firstKey().first)
                            {
                                best_results.put(new Pair<>(curr.second, curr.first), new Pair<Boolean, Long>(false, 0L));
                            }

                        }

                        k = 0;
                        l = 0;
                        curr_list.clear();
                        combinations.clear();
                    }

                }
                continue;
            }
            else
            {
                combinations.add(new Pair<>(a, b));
            }

            var res = gen2_5Opt(path, new Pair<>(a, b), tabo_list);

            for (var x : res)
            {
                if (isPathValid(x))
                {
                    double distance = calculateDistance(x);
                    best_results.put(new Pair<>(calculateDistance(x), x), new Pair<>(false, 0L));

                    if (distance < curr.second)
                    {
                        if (distance < new_curr.second)
                        {
                            new_curr.first = x;
                            new_curr.second = distance;
                        }
                    }

                    curr_list.add(new Pair<>(distance, x));
                }
            }

            while (best_results.size() > best_results_max_size)
            {
                best_results.remove(best_results.descendingKeySet().first());
            }

            if (new_curr.second < curr.second)
            {
                var tmp = new_curr;
                new_curr = curr;
                curr = tmp;

                k = 0;
                l = 0;
                curr_list.clear();
                combinations.clear();
            }

            counter++;
        }

        var best = best_results.firstKey().second;

        List<AbstractMap.SimpleEntry<Product, JobType>> result = new ArrayList<AbstractMap.SimpleEntry<Product, JobType>>();
        for (var elem : best) {
            result.add(products.get(elem));
        }

        return result;
    }
}
