import './ProductList.css';
import React, { Component } from 'react';
import base64url from "base64url";

export class ProductList extends Component {

    api_url = "";
    in_progres = true;
    auth_headers = new Headers();

    name = "";
    type_name = "";
    man_name = "";
    max_weight = 0.0;
    min_weight = 0.0;
    sort = "";
    rev_print = false;

    quey = "";
    quey_data= "";
    checkboxes_state = [false, false, false, false, false];

    constructor(props) {
        super(props);
        this.state = { ready: false, keycloak: props.keycloak, state: "" };
        this.api_url = "http://127.0.0.1/api/";
        this.auth_headers.append('Content-Type', 'application/x-www-form-urlencoded');
        this.auth_headers.append('Authorization', 'bearer ' + this.state.keycloak.token);

        this.searchButton = this.searchButton.bind(this);
    }

    searchButton() {
        this.quey = "";
        this.quey_data = "";

        if (this.checkboxes_state[0] === true) {
            this.quey += "name,";
            this.quey_data += "name=" + encodeURI("%" + this.name + "%") + "&";
        }
        if (this.checkboxes_state[1] === true) {
            this.quey += "weight,";
            this.quey_data += "max_weight=" + encodeURI(this.max_weight) + "&min_weight=" + encodeURI(this.min_weight) + "&";
        }
        if (this.checkboxes_state[2] === true) {
            this.quey += "manufacturer_name,";
            this.quey_data += "manufacturer_name=" + encodeURI("%" + this.man_name + "%") + "&";
        }
        if (this.checkboxes_state[3] === true) {
            this.quey += "type_name,";
            this.quey_data += "type_name=" + encodeURI("%" + this.type_name + "%") + "&";
        }
        if (this.checkboxes_state[4] === true) {
            this.quey += "sort,";
            this.quey_data += "sort=" + this.sort + "&";
        }

        this.in_progres = true;
        this.setState({ ready: false });
    }

    async componentDidMount() {
        this.getProductData()
    }

    async getProductData() {
        try {
            this.quey += ',';

            const response = await fetch(this.api_url + 'product_data/search/true/' + base64url(this.quey), {
                method: 'POST',
                headers: this.auth_headers,
                body: this.quey_data
            });
            const json = await response.json();
            if (response.status !== 200) {
                this.setState({ ready: true, state: "error" });
            } else {
                this.setState({ ready: true, data: json });
            }
        } catch (error) {
            this.setState({ ready: true, state: "error" });
        }
    };

    render() {
        if (!this.state.ready) {
            if (this.in_progres) {
                this.getProductData();
                this.in_progres = false;
            }

            return (
                <>
                    <p>Ładowanie</p>
                </>
            );
        }

        if (this.state.ready) {
            return (
                <>
                    <div class="ProductList">
                        <div class="ProductSearch">
                            <div>
                                <input class="checkbox" defaultChecked={this.checkboxes_state[0]} type="checkbox" onChange={e => this.checkboxes_state[0] = e.target.checked}></input>
                                <label htmlFor="fname">Nazwa produktu:</label>
                                <input type="text" defaultValue={this.name} id="fname" name="fname" onChange={e => this.name = e.target.value}></input>
                            </div>
                            <div>
                                <input class="checkbox" defaultChecked={this.checkboxes_state[1]} type="checkbox" onChange={e => this.checkboxes_state[1] = e.target.checked}></input>
                                <label htmlFor="fweightmin">Waga minimalna produktu:</label>
                                <input type="number" defaultValue={this.min_weight} id="fweightmin" name="fweightmin" step="0.01" min="0.0" onChange={e => this.min_weight = e.target.value}></input>
                                <label htmlFor="fweightmax">Waga maksymalna produktu:</label>
                                <input type="number" defaultValue={this.max_weight} id="fweightmax" name="fweightmax" step="0.01" min="0.0" onChange={e => this.max_weight = e.target.value}></input>
                            </div>
                            <div>
                                <input class="checkbox" defaultChecked={this.checkboxes_state[2]} type="checkbox" onChange={e => this.checkboxes_state[2] = e.target.checked}></input>
                                <label htmlFor="fmname">Nazwa producenta:</label>
                                <input type="text" defaultValue={this.man_name} id="fmname" name="fmname" onChange={e => this.man_name = e.target.value}></input>
                            </div>
                            <div>
                                <input class="checkbox" defaultChecked={this.checkboxes_state[3]} type="checkbox" onChange={e => this.checkboxes_state[3] = e.target.checked}></input>
                                <label htmlFor="ftname">Nazwa typu:</label>
                                <input type="text" defaultValue={this.type_name} id="ftname" name="ftname" onChange={e => this.type_name = e.target.value}></input>
                            </div>
                            <div>
                                <input class="checkbox" defaultChecked={this.checkboxes_state[4]} type="checkbox" onChange={e => this.checkboxes_state[4] = e.target.checked}></input>
                                <label htmlFor="sort">Sortowanie</label>
                                <select defaultValue={this.sort} name="sort_type" id="sort_type" onChange={e => this.sort = e.target.value}>
                                    <option value="name">Nazwa produktu</option>
                                    <option value="type_name">Nazwa typu</option>
                                    <option value="manufacturer_name">Nazwa producenta</option>
                                </select>
                                <label htmlFor="sort_rev" class="rev_label">Odwóć wynik</label>
                                <input name="sort_rev" class="checkbox" defaultChecked={this.rev_print} type="checkbox" onChange={e => this.rev_print = e.target.checked}></input>
                            </div>
                            <div>
                                <button type="button" class="button" onClick={this.searchButton}>Szukaj</button>
                            </div>
                        </div >


                        <ProductListShow data={this.state.data} auth_headers={this.auth_headers} api_url={this.api_url} rev_print={this.rev_print}/>
                    </div>
                </>
            )
        }
    }
}
export default ProductList



function ProductListShow(props) {

    var tableItems = props.data.product_data.map((product, index) =>
        <ProductRow key={index} product={product} api_url={props.api_url} auth_headers={props.auth_headers} />
    );

    if (props.rev_print) {
        tableItems.reverse();
    }

    return (
        <table class="ProductListTable">
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

