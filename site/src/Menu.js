import React from 'react';
import { Navigation } from 'react-minimal-side-navigation';
import 'react-minimal-side-navigation/lib/ReactMinimalSideNavigation.css';



function Menu(props) {
  var items = [];

  if (props.roles.includes("manager")) {
    items.push({
      title: 'Lista Produktów',
      itemId: '/productList',
    });
  }

  if (props.roles.includes("manager")) {
    items.push({
      title: 'Edytuj',
      itemId: '/add',

      subNav: [
        {
          title: 'Produkt',
          itemId: '/add/product'
        },
        {
          title: 'Producent',
          itemId: '/add/manufacturer',
        },
        {
          title: 'Typ Produktu',
          itemId: '/add/productType',
        },
      ],

    });
  }

  items.push({
    title: 'Mapa',
    itemId: '/map',
  });

  if (props.roles.includes("user")) {
    items.push( {
      title: 'Zadanie',
      itemId: '/job',
    });
  }

  if (props.roles.includes("manager")) {
    items.push({
      title: 'Raporty',
      itemId: '/raport',

      subNav: [
        {
          title: 'Raport typ 1',
          itemId: '/raportType1',
        },
        {
          title: 'Raport typ 2',
          itemId: '/raportType2',
        },
      ],
    });
  }

  items.push({
    title: 'Wyloguj',
    itemId: '/logout',
  });

  return (
    <>
      <Navigation
        onSelect={({ itemId }) => {
          props.handler(itemId)
          
        }}
        items={items}
      />
    </>
  );

  return (
    <>
      <Navigation
        onSelect={({ itemId }) => {
          props.handler(itemId)
          
        }}
        items={[
          {
            title: 'Lista Produktów',
            itemId: '/productList',
          },
          {
            title: 'Edytuj',
            itemId: '/add',

            subNav: [
              {
                title: 'Produkt',
                itemId: '/add/product'
              },
              {
                title: 'Producent',
                itemId: '/add/manufacturer',
              },
              {
                title: 'Typ Produktu',
                itemId: '/add/productType',
              },
            ],

          },
          {
            title: 'Mapa',
            itemId: '/map',

          },
          {
            title: 'Zadanie',
            itemId: '/job',

          },
          {
            title: 'Raporty',
            itemId: '/raport',

            subNav: [
              {
                title: 'Raport typ 1',
                itemId: '/raportType1',
              },
              {
                title: 'Raport typ 2',
                itemId: '/raportType2',
              },
            ],
          },
          {
            title: 'Wyloguj',
            itemId: '/logout',
          },
        ]}
      />
    </>
  );

}
export default Menu;
