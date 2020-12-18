import './Edit.css';

import React, { Component } from 'react'
import base64url from "base64url";

class ProductDataEdit extends Component {
    api_url = "";
    in_progres = false;
    auth_headers = new Headers();
    name = "";
    type_name = "";
    man_name = "";
    max_weight = 0.0;
    min_weight = 0.0;
    sort = "";

    rev_print = false;

    new_name = ""
    new_weight = 0.0
    quey = "";
    quey_data= "";
    checkboxes_state = [false, false, false, false, false];

    constructor(props) {
        super(props);
        this.state = { ready: true, keycloak: props.keycloak, state: "show", shearch_id: 0};

        this.api_url = "http://127.0.0.1/api/";
        this.in_progres = false;

        this.auth_headers.append('Content-Type', 'application/x-www-form-urlencoded');
        this.auth_headers.append('Authorization', 'bearer ' + this.state.keycloak.token);

        this.searchButton = this.searchButton.bind(this);
        this.editButton = this.editButton.bind(this);
        this.saveButton = this.saveButton.bind(this);
        this.saveNewButton = this.saveNewButton.bind(this);
        this.addButton = this.addButton.bind(this);
        this.returnButton = this.returnButton.bind(this);
        this.deleteButton = this.deleteButton.bind(this);
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
        this.setState({ ready: false, state: "show" });
    }

    editButton(ID) {
        this.in_progres = true;
        this.setState({ ready: false, state: "edit", shearch_id: ID })
    }
    
    addButton() {
        this.setState({ ready: true, state: "add" })
    }

    saveButton() {
        this.updateProductData1(this.state.shearch_id, this.new_name, this.new_weight);
    }

    deleteButton() {
        this.delProductData(this.state.shearch_id);
    }

    saveNewButton() {
        this.addManufacturer(this.new_name);
    }

    returnButton() {
        this.setState({ ready: true, state: "show" })
    }

    async getProductsData(name) {
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
                this.setState({ ready: true, values: json, state: "show" });
            }
        } catch (error) {
            this.setState({ ready: true, state: "error" });
        }
    };

    async getProductData(id) {
        try {
            const response = await fetch(this.api_url + 'product_data/id/' + id + "/true/", {
                method: 'GET',
                headers: this.auth_headers
            });
            const json = await response.json();
            if (response.status !== 200) {
                this.setState({ ready: true, state: "error" });
            } else {
                this.setState({ ready: true, value: json, state: "edit" });
            }
        } catch (error) {
            this.setState({ ready: true, state: "error" });
        }
    };

    async updateProductData1(id, name, weight) {
        try {
            const data = "name=" + encodeURI(name) + "&weight=" + weight;

            const response = await fetch(this.api_url + 'product_data/id/' + id, {
                method: 'PUT',
                headers: this.auth_headers,
                body: data
            });
            if (response.status !== 201) {
                console.log("err");
                this.setState({ ready: true, state: "error" });
            } else {
                alert("Zapisano");
                this.in_progres = true;
                this.setState({ ready: false, state: "show" });
            }
        } catch (error) {
            console.log(error);
            this.setState({ ready: true, state: "error" });
        }
    };

    async addManufacturer(name) {
        try {
            const data = "name=" + name;

            const response = await fetch(this.api_url + 'manufacturer/add/', {
                method: 'PUT',
                headers: this.auth_headers,
                body: data
            });
            if (response.status !== 201) {
                console.log("err");
                this.setState({ ready: true, state: "error" });
            } else {
                alert("Zapisano");
                this.in_progres = true;
                this.setState({ ready: false, state: "show" });
            }
        } catch (error) {
            console.log(error);
            this.setState({ ready: true, state: "error" });
        }
    };

    async delProductData(id) {
        try {
            const response = await fetch(this.api_url + 'product_data/id/' + id, {
                method: 'DELETE',
                headers: this.auth_headers,
            });
            if (response.status !== 204) {
                console.log("err");
                this.setState({ ready: true, state: "error" });
            } else {
                alert("Usunięto");
                this.in_progres = true;
                this.setState({ ready: false, state: "show" });
            }
        } catch (error) {
            console.log(error);
            this.setState({ ready: true, state: "error" });
        }
    };

    
    render() {
        if (!this.state.ready && this.in_progres) {
            this.in_progres = false;
            if (this.state.state === 'show') {
                this.getProductsData("%" + this.name + "%");
            }
            else if (this.state.state === 'edit') {
                this.getProductData(this.state.shearch_id);
            }
        }

        if (!this.state.ready) {
            return (
                <>
                </>
            );
        }
        else if (this.state.state === 'show') {
            return (
                <>
                <div class="Objects">
                    <div class="Search">
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
                            <label htmlFor="sort_rev">Odwóć wynik</label>
                            <input name="sort_rev" class="checkbox" defaultChecked={this.rev_print} type="checkbox" onChange={e => this.rev_print = e.target.checked}></input>
                        </div>
                        <div>
                            <button type="button" class="search_button" onClick={this.searchButton}>Szukaj</button>
                        </div>
                    </div >

                    <ProductDataList json={this.state.values} callback={this.editButton} rev_print={this.rev_print}/>
                </div>
                </>
            );
        }
        else if (this.state.state === 'edit') {
            this.new_name = this.state.value.name
            this.new_weight = this.state.value.weight
            return (
                <>
                <div class="Objects">
                    <button type="button" class="return_button" onClick={this.returnButton}>Powrót</button>
                    <form class="Edit">
                        <div>
                        <label htmlFor="f2name">Nowa nazwa produktu:</label>
                        <input type="text" defaultValue={this.new_name} id="f2name" name="f2name" onChange={e => this.new_name = e.target.value}></input>
                        </div>
                        <div>
                        <label htmlFor="f2name">Nowa waga produktu:</label>
                        <input type="number" defaultValue={this.new_weight} id="f2name" name="f2name" onChange={e => this.new_weight = e.target.value}></input>
                        </div>
                        <div>
                            <button type="button" class="save_button" onClick={this.saveButton}>Zapisz</button>
                            <button type="button" class="delete_button" onClick={this.deleteButton}>Usuń</button>
                        </div>
                    </form>
                </div>
                </>
            );
        }/*
        else if (this.state.state === 'add') {
            this.new_name = ""
            return (
                <>
                <div class="Objects">
                    <button type="button" class="return_button" onClick={this.returnButton}>Powrót</button>
                    <form class="Edit">
                        <label htmlFor="f3name">Nazwa producenta:</label>
                        <input type="text" defaultValue="" id="f3name" name="f3name" onChange={e => this.new_name = e.target.value}></input>
                        <div><button type="button" class="save_button" onClick={this.saveNewButton}>Zapisz</button></div>
                    </form >
                </div>
                </>
            );
        }*/
        else if (this.state.state === 'error') {
            return (
                <>
                <p>Błąd</p>
                </>
            );
        }
    }
}

export default ProductDataEdit;

function ProductDataList(props) {
    var product_data_info = [];

    if (props.json == null) {
        return (<></>);
    }

    for (var product_data of props.json.product_data) {
        product_data_info.push(
            <div class="SingleObject" key={product_data.ID}>
                <div>Nazwa Produktu: {product_data.name} waga: {product_data.weight} <button id={product_data.ID} type="button" class="edit_button" onClick={e => props.callback(e.target.id)}>Edytuj</button></div>
                <div>Nazwa Typu: {product_data.type.name}  <button id={product_data.ID} type="button" class="edit_button" >Edytuj</button></div>
                <div>Nazwa Producenta: {product_data.manufacturer.name} <button id={product_data.ID} type="button" class="edit_button" >Edytuj</button></div>
            </div>
        );
    }

    if (props.rev_print) {
        product_data_info.reverse();
    }

    return product_data_info;
}
