import './ProductList.css';
import React, { Component } from 'react';


export class ProductList extends Component {

    api_url = "";
    in_progres = true;
    auth_headers = new Headers();

    constructor(props) {
        super(props);
        this.state = { ready: false, keycloak: props.keycloak, state: "" };
        this.api_url = "http://127.0.0.1/api/";
        this.auth_headers.append('Authorization', 'bearer ' + this.state.keycloak.token);
    }

    async componentDidMount() {
        this.getProductData()
    }

    async getProductData() {
        try {
            const response = await fetch(this.api_url + 'product_data/all/true/', {
                method: 'GET',
                headers: this.auth_headers
            });
            const json = await response.json();
            if (response.status === 200) {
                this.setState({ ready: true, data: json });
            }
        } catch (error) {
            this.setState({ ready: true, state: 'error' });
        }
    };

    render() {
        if (!this.state.ready) {
            return (
                <>
                    <p>Ładowanie</p>
                </>
            );
        }

        if (this.state.ready) {
            return (
                <>
                    <div>
                        <ProductListShow data={this.state.data} auth_headers={this.auth_headers} api_url={this.api_url} />
                    </div>
                </>
            )
        }
    }
}
export default ProductList



function ProductListShow(props) {

    const tableItems = props.data.product_data.map((product, index) =>
        <ProductRow key={index} product={product} api_url={props.api_url} auth_headers={props.auth_headers} />
    );

    return (
        <table>
            <thead>
                <tr>
                    <th>Nazwa</th>
                    <th>ID</th>
                    <th>Waga</th>
                    <th>Producent</th>
                    <th>Typ</th>
                    <th>Dodaj do magazynu</th>
                    <th>Dodaj do wysłania</th>
                </tr>
            </thead>
            <tbody>
                {tableItems}
            </tbody>
        </table>
    )
}

function ProductRow(props) {

    return (
        <tr key={props.product.id}>
            <td>{props.product.name} </td>
            <td>{props.product.ID} </td>
            <td>{props.product.weight.toFixed(2)} </td>
            <td>{props.product.manufacturer.name} </td>
            <td>{props.product.type.name}  </td>
            <td className='button' onClick={addProduct(props.product.ID, 1, props.api_url, props.auth_headers)}>Dodaj</td>
            <td className='button' onClick={removeProduct(props.product.ID, 1, props.api_url, props.auth_headers)}>Dodaj</td>
        </tr>

    )
}

function addProduct(id, amount, api_url, auth_headers) {
    return async function () {

        var data = new URLSearchParams()
        data.append('id', id)
        const response = await fetch(api_url + 'storage/add/', {
            method: 'PUT',
            headers: auth_headers,
            body: data,
        });

        if (response.ok) {
            alert('Dodano produkt o ID: ' + id)
        }
        else {
            alert('Błąd: nie dodano produktu')
        }
    }
}

function removeProduct(id, amount, api_url, auth_headers) {
    return async function () {

        var data = new URLSearchParams()
        data.append('id', id)
        const response = await fetch(api_url + 'storage/remove/', {
            method: 'DELETE',
            headers: auth_headers,
            body: data,
        });

        if (response.ok) {
            alert('Dodano produkt o ID do wysłania: ' + id)
        }
        else {
            alert('Błąd: nie dodano produktu do wysłania')
        }
    }
}

