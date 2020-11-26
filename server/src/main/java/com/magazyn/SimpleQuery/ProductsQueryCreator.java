package com.magazyn.SimpleQuery;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProductsQueryCreator implements QueryCreator {
    public enum QUERY_TYPE { GET, GET_JOIN /*JOIN with other tables*/ };
    private QUERY_TYPE query_type;

    private class Command {
        public String operation = "";
        public long priority;
        public String type = "";
    }

    public ProductsQueryCreator(QUERY_TYPE query_type) {
        this.query_type = query_type;
    }

    @Override
    public String fromKeyWords(String key_words) {
        List<String> args = Arrays.asList(key_words.split(","));
        args = args.stream().filter(x -> x.length() > 0).collect(Collectors.toList());

        switch (query_type) {
            case GET:
            return createGETQuery(args, false);
        case GET_JOIN:
            return createGETQuery(args, true);
        }   


        return null;
    }
    
    private String createGETQuery(List<String> arguments, boolean joined) {
        List<Command> commands = arguments.stream().map(x -> {
            String[] command_data = x.split("\\|");
            if (command_data.length != 3) {
                throw new IllegalArgumentException("Commad's data is invalid (E1): \'" + x + "\'");
            }
            Command cmd = new Command();
            cmd.operation = command_data[0];
            cmd.type = command_data[2];
            try {
                cmd.priority = Long.parseLong(command_data[1]);
            } catch (NumberFormatException exception) {
                throw new IllegalArgumentException("Commad's data is invalid (E2): \'" + x + "\'");
            }

            return cmd;
        }).sorted((left, right) -> {
            return Long.compare(left.priority, right.priority);
        }).collect(Collectors.toList());

        StringBuilder txt = new StringBuilder();
        for (Command cmd : commands) {
            txt.append(cmd.operation).append("|").append(cmd.priority).append("|").append(cmd.type).append(",");
        }

        //TODO DB is required to continue

        return txt.toString();
    }
}
