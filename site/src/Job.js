import './Job.css';

import React, { Component } from 'react'
import Keycloak from 'keycloak-js';

class Job extends Component {
    api_url = "";
    in_progres = true;
    auth_headers = new Headers();

    constructor(props) {
        super(props);
        this.state = { ready: false, keycloak: props.keycloak, state: "" };

        this.api_url = "http://127.0.0.1/api/";
        this.in_progres = true;

        this.newJobButton = this.newJobButton.bind(this);
        this.confirmJobButton = this.confirmJobButton.bind(this);

        this.auth_headers.append('Content-Type', 'application/json');
        this.auth_headers.append('Authorization', 'bearer ' + this.state.keycloak.token);
    }

    async getJobStatus() {
        try {
            const response = await fetch(this.api_url + 'job/me/', {
                method: 'GET',
                headers: this.auth_headers
            });
            const json = await response.json();
            if (response.status === 404) {
                this.setState({ ready: true, state: "no_job" });
            } else {
                this.setState({ ready: true, values: json, state: "in_job" });
            }
        } catch (error) {
            this.setState({ ready: true, state: "error" });
        }
    };

    async generateNewJob() {
        try {
            const response = await fetch(this.api_url + 'job/gen/', {
                method: 'PUT',
                headers: this.auth_headers
            });
            const json = await response.json();
            if (response.status === 400) {
                this.setState({ ready: true, state: "error" });
            } else {
                this.setState({ ready: true, values: json, state: "new_job" });
            }
        } catch (error) {
            this.setState({ ready: true, state: "error" });
        }
    };

    async confirmJob() {
        try {
            const response = await fetch(this.api_url + 'job/confirm/', {
                method: 'PUT',
                headers: this.auth_headers
            });
            if (response.status === 400) {
                this.setState({ ready: true, state: "error" });
            } else {
                this.in_progres = true;
                this.setState({ ready: false, state: "" });
                console.log("set");
            }
        } catch (error) {
            console.log(error);
            this.setState({ ready: true, state: "error" });
        }
    };

    newJobButton() {
        this.in_progres = true;
        this.setState({ ready: false, state: "new_job" });
    }

    confirmJobButton() {
        this.in_progres = true;
        this.setState({ ready: false, state: "conf_job" });
    }
    
    render() {
        if (!this.state.ready && this.in_progres) {
            this.in_progres = false;
            if (this.state.state === '') {
                this.getJobStatus();
            } else if (this.state.state === 'new_job') {
                this.generateNewJob();
            } else if (this.state.state === 'conf_job') {
                this.confirmJob();
            }
        }

        if (!this.state.ready) {
            return (
                <>
                <p>Zadania</p>
                </>
            );
        }
        else if (this.state.state === 'in_job') {
            return (
                <>
                <div class="Job">
                <ConfirmJob caller={this}/>
                <CurrentJobList json={this.state.values}/>
                </div>
                </>
            );
        }
        else if (this.state.state === 'no_job') {
            return (
                <> <div class="Job">
                <NewJob caller={this}/>
                </div> </>
            );
        } 
        else if (this.state.state === 'new_job') {
            if (this.state.values.count === 0) {
                return (
                    <div class="Job">
                        <div class="SingleJob">
                        <p>Brak zadań do wykonania</p>
                        </div>
                    </div>
                );
            }
            return (
                <>
                <div class="Job">
                <ConfirmJob caller={this}/>
                <NewJobList json={this.state.values}/>
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

export default Job;

function CurrentJobList(props) {
    var job_info = [];
    
    var i = 0;
    for (var job of props.json.job) {
        job_info.push(
            <div class="SingleJob" id={job.id}>
                <p>Produkt:{job.id} Zadanie:{job.type} Miejsce: Szafa {job.location.rack} Pozycja {job.location.place}</p>
            </div>
        );
    }

    return job_info;
}

function NewJobList(props) {
    var count = props.json.count;
    var job_info = [];

    var take_in = false;
    var take_out = false;
    
    for (var i = 0; i < count; i++) {
        var job = props.json[i.toString()];
        if (job.type === 'take_in') { take_in = true; }
        if (job.type === 'take_out') { take_out = true; }

        job_info.push(
            <div class="SingleJob" id={job.ID}>
                <p>Produkt:{job.ID} Zadanie:{job.type} Miejsce: Szafa {job.location.rack} Pozycja {job.location.place}</p>
            </div>
        );

        if (i < count - 1) {
            job_info.push(<div class="arrow" id={"arrow" + i}>⇩</div>);
        }
    }

    if (take_in) {
        job_info.unshift(<div class="arrow" id={"arrow" + i}>⇩</div>);
        job_info.unshift(
            <div class="SingleJob" id={job.ID}>
                <p>Odbierz produkty</p>
            </div>
        );
    }

    if (take_out) {
        job_info.push(<div class="arrow" id={"arrow" + i}>⇩</div>);
        job_info.push(
            <div class="SingleJob" id={job.ID}>
                <p>Przenieś produkty do wysłania</p>
            </div>
        );
    }

    return job_info;
}

function NewJob(props) {
    return (
        <div>
            <button type="button" onClick={props.caller.newJobButton} class="new_job_button">Wygeneruj nowe zadanie</button>
        </div>
    )
}

function ConfirmJob(props) {
    return (
        <div>
            <button type="button" onClick={props.caller.confirmJobButton} class="confirm_job_button">Potwierdź wykonanie</button>
        </div>
    )
}
