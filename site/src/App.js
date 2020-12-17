import './App.css';
import Menu from './Menu'
import Job from './Job'
import React, { Component } from 'react'
import Keycloak from 'keycloak-js';

class App extends Component {



  constructor(props) {
    super(props);
    this.state = { keycloak: null, authenticated: false, pageShown: "/productList" };
    this.handler = this.handler.bind(this)
  }

  handler(itemId) {
    this.setState({
      pageShown: itemId,
      pageToShow: ProductList
    })
  }


  componentDidMount() {
    const keycloak = Keycloak('/keycloak.json');
    keycloak.init({ onLoad: 'login-required' }).then(authenticated => {
      this.setState({ keycloak: keycloak, authenticated: authenticated })
    })
  }





  render() {
    if (this.state.keycloak) {
      if (this.state.authenticated) return (
        <>
          <div class="App">
          <div className='Menu'><Menu handler={this.handler} /></div>
          <Page pageShow={this.state.pageShown} logout={this.state.keycloak.logout} keycloak={this.state.keycloak} />
          </div>
        </>

      ); else return (<div>Unable to authenticate!</div>)
    }
    if (!this.state.authenticated) {
      return (
        <div>Initializing Keycloak...</div>
      );
    }
  }
}


function Page(props) {
  if (props.pageShow === '/productList') return <ProductList />
  if (props.pageShow === '/map') return <Map />
  if (props.pageShow === '/getProduct') return <GetProduct />
  if (props.pageShow === '/add/product') return <AddProduct />
  if (props.pageShow === '/add/manufacturer') return <AddManufacturer />
  if (props.pageShow === '/add/productType') return <AddProductType />
  if (props.pageShow === '/job') return <Job keycloak={props.keycloak} />
  if (props.pageShow === '/raportType1') return <RaportType1 />
  if (props.pageShow === '/raportType2') return <RaportType2 />
  if (props.pageShow === '/logout') return <Logout logout={props.logout} />

}

function Logout(props) {
  props.logout()
}

function ProductList(props) {
  return (
    <>
      <p>Lista Produktów</p>
    </>
  )
}

function Map() {
  //Change to API URL
  return (
    <>
      <div class="Map">
        <img src="http://127.0.0.1/map.png" alt="Mapa"/>
        <div class="Map_info"><div class="square_in"></div><p>Punkt odbioru produktów</p></div>
        <div class="Map_info"><div class="square_out"></div><p>Punkt wysyłki produktów</p></div>
        <div class="Map_info"><div class="square_rack"></div><p>Strona szafki, z której można pobrać produkty </p></div>
      </div>
    </>
  )
}

function GetProduct() {
  return (
    <>
      <p>Odbierz produkt</p>
    </>
  )
}

function AddProduct() {
  return (
    <>
      <p>Dodaj produkt</p>
    </>
  )
}

function AddManufacturer() {
  return (
    <>
      <p>Dodaj producenta</p>
    </>
  )
}

function AddProductType() {
  return (
    <>
      <p>Dodaj typ produktu</p>
    </>
  )
}

function RaportType1() {
  return (
    <>
      <p>Raport Typu 1</p>
    </>
  )
}

function RaportType2() {
  return (
    <>
      <p>Raport Typu 2</p>
    </>
  )
}
export default App;
