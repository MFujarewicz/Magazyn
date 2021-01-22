import './Edit.css';

import React, { Component } from 'react'
import base64url from "base64url";
import SelectSearch from 'react-select-search';

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
    new_type = 0
    new_man = 0
    quey = "";
    quey_data= "";
    checkboxes_state = [false, false, false, false, false];

    constructor(props) {
        super(props);
        this.state = { ready: true, keycloak: props.keycloak, state: "show", shearch_id: 0, edit_ready_1: false, edit_ready_2: false };

        this.api_url = "https://127.0.0.1/api/";
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
        this.updateProductData1(this.state.shearch_id, this.new_name, this.new_weight, this.new_type, this.new_man);
    }

    deleteButton() {
        this.delProductData(this.state.shearch_id);
    }

    saveNewButton() {
        this.addProductData(this.new_name, this.new_weight, this.new_type, this.new_man);
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

    async updateProductData1(id, name, weight, type, manufacturer) {
        try {
            const data = "name=" + encodeURI(name) + "&weight=" + weight + "&type=" + type + "&manufacturer=" + manufacturer;

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

    async addProductData(name, weight, type, manufacturer) {
        try {
            const data = "name=" + encodeURI(name) + "&weight=" + weight + "&type=" + type + "&manufacturer=" + manufacturer;

            const response = await fetch(this.api_url + 'product_data/add/', {
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

    async getAllTypes() {
        try {
            const response = await fetch(this.api_url + 'type/all/', {
                method: 'GET',
                headers: this.auth_headers,
            });
            if (response.status !== 200) {
                console.log("err");
                this.setState({ ready: true, state: "error" });
            } else {
                const json = await response.json();
                this.setState({ edit_ready_1: true, types: json });
            }
        } catch (error) {
            console.log(error);
            this.setState({ ready: true, state: "error" });
        }
    };

    async getAllManufacturers() {
        try {
            const response = await fetch(this.api_url + 'manufacturer/all/', {
                method: 'GET',
                headers: this.auth_headers,
            });
            if (response.status !== 200) {
                console.log("err");
                this.setState({ ready: true, state: "error" });
            } else {
                const json = await response.json();
                this.setState({ edit_ready_2: true, manufacturers: json });
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
                    <p>Proszę czekać</p>
                </>
            );
        }
        else if (this.state.state === 'show') {
            return (
                <>
                <div className="Objects">
                    <div className="Search">
                        <div>
                            <input className="checkbox" defaultChecked={this.checkboxes_state[0]} type="checkbox" onChange={e => this.checkboxes_state[0] = e.target.checked}></input>
                            <label htmlFor="fname">Nazwa produktu:</label>
                            <input type="text" defaultValue={this.name} id="fname" name="fname" onChange={e => this.name = e.target.value}></input>
                        </div>
                        <div>
                            <input className="checkbox" defaultChecked={this.checkboxes_state[1]} type="checkbox" onChange={e => this.checkboxes_state[1] = e.target.checked}></input>
                            <label htmlFor="fweightmin">Waga minimalna produktu:</label>
                            <input type="number" defaultValue={this.min_weight} id="fweightmin" name="fweightmin" step="0.01" min="0.0" onChange={e => this.min_weight = e.target.value}></input>
                            <label htmlFor="fweightmax">Waga maksymalna produktu:</label>
                            <input type="number" defaultValue={this.max_weight} id="fweightmax" name="fweightmax" step="0.01" min="0.0" onChange={e => this.max_weight = e.target.value}></input>
                        </div>
                        <div>
                            <input className="checkbox" defaultChecked={this.checkboxes_state[2]} type="checkbox" onChange={e => this.checkboxes_state[2] = e.target.checked}></input>
                            <label htmlFor="fmname">Nazwa producenta:</label>
                            <input type="text" defaultValue={this.man_name} id="fmname" name="fmname" onChange={e => this.man_name = e.target.value}></input>
                        </div>
                        <div>
                            <input className="checkbox" defaultChecked={this.checkboxes_state[3]} type="checkbox" onChange={e => this.checkboxes_state[3] = e.target.checked}></input>
                            <label htmlFor="ftname">Nazwa typu:</label>
                            <input type="text" defaultValue={this.type_name} id="ftname" name="ftname" onChange={e => this.type_name = e.target.value}></input>
                        </div>
                        <div>
                            <input className="checkbox" defaultChecked={this.checkboxes_state[4]} type="checkbox" onChange={e => this.checkboxes_state[4] = e.target.checked}></input>
                            <label htmlFor="sort">Sortowanie</label>
                            <select defaultValue={this.sort} name="sort_type" id="sort_type" onChange={e => this.sort = e.target.value}>
                                <option value="name">Nazwa produktu</option>
                                <option value="type_name">Nazwa typu</option>
                                <option value="manufacturer_name">Nazwa producenta</option>
                            </select>
                            <label htmlFor="sort_rev" className="rev_label">Odwóć wynik</label>
                            <input name="sort_rev" className="checkbox" defaultChecked={this.rev_print} type="checkbox" onChange={e => this.rev_print = e.target.checked}></input>
                        </div>
                        <div>
                            <button type="button" className="search_button" onClick={this.searchButton}>Szukaj</button>
                        </div>
                    </div >

                    <button type="button" className="add_button" onClick={this.addButton}>Dodaj nowy typ</button>
                    <ProductDataList json={this.state.values} callback={this.editButton} rev_print={this.rev_print}/>
                </div>
                </>
            );
        }
        else if (this.state.state === 'edit') {
            if (this.state.edit_ready_1 && this.state.edit_ready_2) {
                var types = [];
                var mans = [];
                for (var data of this.state.types.types) {
                    types.push({name: data.name, value: data.ID})
                }
                for (var data2 of this.state.manufacturers.manufacturers) {
                    mans.push({name: data2.name, value: data2.ID})
                }
                this.new_name = this.state.value.name
                this.new_weight = this.state.value.weight
                this.new_type = this.state.value.type.ID
                this.new_man = this.state.value.manufacturer.ID
                return (
                    <>
                    <div className="Objects">
                        <button type="button" className="return_button" onClick={this.returnButton}>Powrót</button>
                        <form className="Edit">
                            <div>
                            <label htmlFor="fname">Nowa nazwa produktu:</label>
                            <input type="text" defaultValue={this.new_name} id="fname" name="fname" onChange={e => this.new_name = e.target.value}></input>
                            </div>
                            <div>
                            <label htmlFor="fweight">Nowa waga produktu:</label>
                            <input type="number" defaultValue={this.new_weight} id="fweight" name="fweight" onChange={e => this.new_weight = e.target.value}></input>
                            </div>
                            <div>
                            <label htmlFor="ftype">Nowy typ produktu:</label>
                            <SelectSearch id="ftype" name="ftype" options={types} search value={this.new_type} onChange={e => this.new_type = e}/>
                            </div>
                            <div>
                            <label htmlFor="fman">Nowy typ produktu:</label>
                            <SelectSearch id="fman" name="fman" options={mans} search value={this.new_man} onChange={e => this.new_man = e}/>
                            </div>
                            <div>
                                <button type="button" className="save_button" onClick={this.saveButton}>Zapisz</button>
                            </div>
                        </form>
                    </div>
                    </>
                );
            }
            else {
                if (!this.state.edit_ready_1 || !this.state.edit_ready_2) {
                    this.getAllTypes();
                    this.getAllManufacturers();
                }
                return (
                    <>
                    <p>Proszę czekać</p>
                    </>
                );
            }
        }
        else if (this.state.state === 'add') {
            if (this.state.edit_ready_1 && this.state.edit_ready_2) {
                var types_e = [];
                var mans_e = [];
                for (var data3 of this.state.types.types) {
                    types_e.push({name: data3.name, value: data3.ID})
                }
                for (var data4 of this.state.manufacturers.manufacturers) {
                    mans_e.push({name: data4.name, value: data4.ID})
                }
                this.new_name = "name"
                this.new_weight = 1.0
                this.new_type = types_e[0].value
                this.new_man = mans_e[0].value
                return (
                    <>
                    <div className="Objects">
                        <button type="button" className="return_button" onClick={this.returnButton}>Powrót</button>
                        <form className="Edit">
                            <div>
                            <label htmlFor="fname">Nowa nazwa produktu:</label>
                            <input type="text" defaultValue={this.new_name} id="fname" name="fname" onChange={e => this.new_name = e.target.value}></input>
                            </div>
                            <div>
                            <label htmlFor="fweight">Nowa waga produktu:</label>
                            <input type="number" defaultValue={this.new_weight} id="fweight" name="fweight" onChange={e => this.new_weight = e.target.value}></input>
                            </div>
                            <div>
                            <label htmlFor="ftype">Nowy typ produktu:</label>
                            <SelectSearch id="ftype" name="ftype" options={types_e} search value={this.new_type} onChange={e => this.new_type = e}/>
                            </div>
                            <div>
                            <label htmlFor="fman">Nowy typ produktu:</label>
                            <SelectSearch id="fman" name="fman" options={mans_e} search value={this.new_man} onChange={e => this.new_man = e}/>
                            </div>
                            <div>
                                <button type="button" className="save_button" onClick={this.saveNewButton}>Zapisz</button>
                            </div>
                        </form>
                    </div>
                    </>
                );
            }
            else {
                if (!this.state.edit_ready_1 || !this.state.edit_ready_2) {
                    this.getAllTypes();
                    this.getAllManufacturers();
                }
                return (
                    <>
                    <p>Proszę czekać</p>
                    </>
                );
            }
        }
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
            <div className="SingleObject" key={product_data.ID}>
                <div>Nazwa Produktu: {product_data.name} waga: {product_data.weight} <button id={product_data.ID} type="button" className="edit_button" onClick={e => props.callback(e.target.id)}>Edytuj</button></div>
                <div>Nazwa Typu: {product_data.type.name} </div>
                <div>Nazwa Producenta: {product_data.manufacturer.name}</div>
            </div>
        );
    }

    if (props.rev_print) {
        product_data_info.reverse();
    }

    return product_data_info;
}
