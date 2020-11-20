import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCarrot} from "@fortawesome/free-solid-svg-icons";
import React, {Component} from "react";
import axios from 'axios';

import Register from "../Register/Register";
import './App.css';

class App extends Component {

    state = {
        loggedIn: false
    }

    createUser = () => {
        axios.post('/user', this.state.user, {
            "Content-Type":"application/json"
        })
            .then(res => {
                console.log(res)
            })
            .catch(error => console.log(error));
    }

    render() {
        let homeMessage
        homeMessage = this.state.loggedIn ? "Welcome back, {insert name here}!" : "Welcome! Login or register to proceed"
        return(
            <div className="App">

                <div>
                    <header className="App-header">
                        <h1>Kitchen app</h1>
                        <FontAwesomeIcon icon={faCarrot}/>
                        <p>{homeMessage}</p>
                    </header>
                </div>

                <div>
                    <Register/>
                </div>
            </div>
        );
    }
}

export default App;
