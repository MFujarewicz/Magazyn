import './Edit.css';

import React, { Component } from 'react'
import base64url from "base64url";

class TypeEdit extends Component {
    api_url = "";
    in_progres = false;
    auth_headers = new Headers();
    name = ""
    new_name = ""

    constructor(props) {
        super(props);
        this.state = { ready: true, keycloak: props.keycloak, state: "show", shearch_id: 0};

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
        this.in_progres = true;
        this.setState({ ready: false, state: "show" })
    }

    editButton(ID) {
        this.in_progres = true;
        this.setState({ ready: false, state: "edit", shearch_id: ID })
    }

    addButton() {
        this.setState({ ready: true, state: "add" })
    }

    saveButton() {
        this.updateType(this.state.shearch_id, this.new_name);
    }

    deleteButton() {
        this.delType(this.state.shearch_id);
    }

    saveNewButton() {
        this.addType(this.new_name);
    }

    returnButton() {
        this.setState({ ready: true, state: "show" })
    }

    async getTypes(name) {
        try {
            const response = await fetch(this.api_url + 'type/name/' + base64url(name), {
                method: 'GET',
                headers: this.auth_headers
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

    async getType(id) {
        try {
            const response = await fetch(this.api_url + 'type/id/' + id, {
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

    async updateType(id, name) {
        try {
            const data = "name=" + name;

            const response = await fetch(this.api_url + 'type/id/' + id, {
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

    async addType(name) {
        try {
            const data = "name=" + name;

            const response = await fetch(this.api_url + 'type/add/', {
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

    async delType(id) {
        try {
            const response = await fetch(this.api_url + 'type/id/' + id, {
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
                this.getTypes("%" + this.name + "%");
            }
            else if (this.state.state === 'edit') {
                this.getType(this.state.shearch_id);
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
                <div className="Objects">
                    <form className="Search">
                        <label htmlFor="f1name">Nazwa typu:</label>
                        <input type="text" defaultValue={this.name} id="f1name" name="f1name" onChange={e => this.name = e.target.value}></input>
                        <button type="button" className="search_button" onClick={this.searchButton}>Szukaj</button>
                    </form >

                    <button type="button" className="add_button" onClick={this.addButton}>Dodaj nowy typ</button>

                    <TypeList json={this.state.values} callback={this.editButton}/>
                </div>
                </>
            );
        }
        else if (this.state.state === 'edit') {
            this.new_name = this.state.value.name
            return (
                <>
                <div className="Objects">
                    <button type="button" className="return_button" onClick={this.returnButton}>Powrót</button>
                    <form className="Edit">
                        <label htmlFor="f2name">Nowa nazwa typu:</label>
                        <input type="text" defaultValue={this.new_name} id="f2name" name="f2name" onChange={e => this.new_name = e.target.value}></input>
                        <div>
                            <button type="button" className="save_button" onClick={this.saveButton}>Zapisz</button>
                        </div>
                    </form >
                </div>
                </>
            );
        }
        else if (this.state.state === 'add') {
            this.new_name = ""
            return (
                <>
                <div className="Objects">
                    <button type="button" className="return_button" onClick={this.returnButton}>Powrót</button>
                    <form className="Edit">
                        <label htmlFor="f3name">Nazwa typu:</label>
                        <input type="text" defaultValue="" id="f3name" name="f3name" onChange={e => this.new_name = e.target.value}></input>
                        <div><button type="button" className="save_button" onClick={this.saveNewButton}>Zapisz</button></div>
                    </form >
                </div>
                </>
            );
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

export default TypeEdit;

function TypeList(props) {
    var type_info = [];

    if (props.json == null) {
        return (<></>);
    }

    for (var type of props.json.types) {
        type_info.push(
            <div className="SingleObject" key={type.ID}>
                Nazwa typu: {type.name} <button id={type.ID} type="button" className="edit_button" onClick={e => props.callback(e.target.id)}>Edytuj</button>
            </div>
        );
    }

    return type_info;
}