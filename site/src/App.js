import './App.css';
import Menu from './Menu'
import Job from './Job'
import TypeEdit from './TypeEdit'
import ManufacturerEdit from './ManufacturerEdit'
import ProductDataEdit from './ProductDataEdit'
import ProductList from "./ProductList";
import React, { Component } from 'react'
import Keycloak from 'keycloak-js';


class App extends Component {

  constructor(props) {
    super(props);
    this.state = { keycloak: null, authenticated: false, pageShown: "/home" };
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
          <div className="App">
            <div className='Menu'><Menu handler={this.handler} roles={this.state.keycloak.tokenParsed.resource_access.Warehouse.roles} name={this.state.keycloak.tokenParsed.preferred_username} /></div>
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
  if (props.pageShow === '/productList') return <ProductList keycloak={props.keycloak}/>
  if (props.pageShow === '/map') return <Map />
  if (props.pageShow === '/home') return <Home keycloak={props.keycloak}/>
  if (props.pageShow === '/add/product') return <ProductDataEdit keycloak={props.keycloak} />
  if (props.pageShow === '/add/manufacturer') return <ManufacturerEdit keycloak={props.keycloak} />
  if (props.pageShow === '/add/productType') return <TypeEdit keycloak={props.keycloak} />
  if (props.pageShow === '/job') return <Job keycloak={props.keycloak} />
  if (props.pageShow === '/raportType1') return <RaportType1 />
  if (props.pageShow === '/raportType2') return <RaportType2 />
  if (props.pageShow === '/logout') return <Logout logout={props.logout} />

}

function Logout(props) {
  props.logout()
}

function Map() {
  //Change to API URL
  //Change request with token
  return (
    <>
      <div className="Map">
        <img src="https://127.0.0.1/map.png" alt="Mapa" />
        <div className="Map_info"><div className="square_in"></div><p>Punkt odbioru produktów</p></div>
        <div className="Map_info"><div className="square_out"></div><p>Punkt wysyłki produktów</p></div>
        <div className="Map_info"><div className="square_rack"></div><p>Strona szafki, z której można pobrać produkty </p></div>
      </div>
    </>
  )
}

function Home(props) {
  //Change to API URL
  return (
    <>
      <div className="Home">
        <img src="https://127.0.0.1/home.svg" alt="Home" />
      </div>
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
