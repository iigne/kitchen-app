import React, {Component} from "react";
import axios from 'axios';

import Register from "../Register/Register";
import './App.css';
import { BrowserRouter as Router, Switch, Route, Link } from 'react-router-dom';
import Home from "../Home/Home";
import {Navbar, Nav} from "react-bootstrap";
import Login from "../Login";

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
        return(
            <div className="App">
                <Router>
                    <Navbar bg="light">
                        <Navbar.Brand as={Link} to="/" className="nav-link">Kitchen app</Navbar.Brand>
                        <Nav.Link as={Link} to="/register" className="nav-link">Register</Nav.Link>
                        <Nav.Link as={Link} to="/login">Login</Nav.Link>
                    </Navbar>
                    <Switch>
                        <Route exact path="/" component={Home}/>
                        <Route path="/register" component={Register}/>
                        <Route path="/login" component={Login}/>
                    </Switch>
                </Router>
            </div>
        );
    }
}

export default App;
