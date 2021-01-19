import React from 'react';
import { Navigation } from 'react-minimal-side-navigation';
import 'react-minimal-side-navigation/lib/ReactMinimalSideNavigation.css';



function Menu(props) {

  var menuItems = []

  if (props.roles.includes("user") || props.roles.includes("manager") || props.roles.includes("administrator")) {
    menuItems.push(
      {
        title: 'Zadania',
        itemId: '/job',
      },
      {
        title: 'Mapa',
        itemId: '/map',
      },
    )
  }

  if (props.roles.includes("manager") || props.roles.includes("administrator")) {
    menuItems.push(
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
    )
  }

  if (props.roles.includes("administrator")) {
    menuItems.push()
  }

  menuItems.push({
    title: 'Wyloguj',
    itemId: '/logout',
  })

  return (
    <>
      <Navigation
        onSelect={({ itemId }) => {
          props.handler(itemId)

        }}
        items={menuItems}
      />
    </>
  );

}
export default Menu;
