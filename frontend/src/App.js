import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCarrot} from "@fortawesome/free-solid-svg-icons";
import React, {Component} from "react";
import Button from "react-bootstrap/Button"
import axios from 'axios';


import './App.css';

class App extends Component {

    state = {
        ingredients: [],
        users: [],
        user: {
            email: "email@email.com",
            username: "user123444412312",
            password: "$2b$10$5ARJkonpoxXNxibg5O1hZ.uoSclPGKdItM6v4QeKQ9oUKmTjHwn96"
        }
    }

    callIngredientsEndpoint = () => {
        axios.get('/ingredient/all')
            .then (res => {
                console.log(res);
                const ingredients = res.data;
                this.setState({ingredients});
            })
            .catch(error => console.log(error));
    }

    callUsersEndpoint = () => {
        axios.get('/user/all')
            .then (res => {
                console.log(res);
                const users = res.data;
                this.setState({users});
            })
            .catch(error => console.log(error));
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
        return(
            <div className="App">

                <div>
                    <header className="App-header">
                        <FontAwesomeIcon icon={faCarrot}/>
                        <p>Hello from kitchen app!</p>
                    </header>
                </div>

                <div>
                    <Button className="App-button" variant="primary" onClick={this.callIngredientsEndpoint}>Get ingredients from DB</Button>{' '}
                </div>
                <div>
                    <p>Clicking will make a call to the database and list the items that are currently in the ingredients table</p>
                    <ul>
                        { this.state.ingredients.map(ingredient => <li>{ingredient.name}</li>)}
                    </ul>

                </div>
                <div>
                    <h2>list users</h2>
                    <Button variant="outline-primary" onClick={this.callUsersEndpoint}>list users</Button>
                    <ul> {this.state.users.map(u => <li>{u.username + ' ' +u.email}</li>)}</ul>
                </div>
                <div>
                    <h2>create user</h2>
                    <form>
                        <Button type="submit" variant={"outline-secondary"} onClick={this.createUser}>submit</Button>
                    </form>
                </div>
            </div>
        );
    }
}

export default App;
