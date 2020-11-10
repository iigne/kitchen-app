import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCarrot} from "@fortawesome/free-solid-svg-icons";
import React, {Component} from "react";
import Button from "react-bootstrap/Button"
import axios from 'axios';


import './App.css';

class App extends Component {

    state = {
        ingredients: []
    }

    callEndpoint = () => {
        axios.get('/ingredient/all')
            .then (res => {
                console.log(res);
                const ingredients = res.data;
                this.setState({ingredients});
            })
            .catch(error => console.log(error));
    }

    render() {
        return(
            <div className="App">

                <div>
                    <header className="App-header">
                        <FontAwesomeIcon icon={faCarrot}/>
                        <p>Hello from kitchen app!</p>
                    </header>
                </div>

                <div>
                    <Button className="App-button" variant="primary" onClick={this.callEndpoint}>Get ingredients from DB</Button>{' '}
                </div>
                <div>
                    <p>Clicking will make a call to the database and list the items that are currently in the ingredients table</p>
                    <ul>
                        { this.state.ingredients.map(ingredient => <li>{ingredient.name}</li>)}
                    </ul>

                </div>
            </div>
        );
    }
}

export default App;
