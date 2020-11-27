import {Nav, Navbar} from "react-bootstrap";
import {BrowserRouter as Router, Link, Route, Switch} from "react-router-dom";
import Home from "../Home/Home";
import Register from "../Register/Register";
import Login from "../Login/Login";
import React from "react";
import PrivateRoute from "./PrivateRoute";
import UserStock from "../UserStock/UserStock";
import PublicRoute from "./PublicRoute";

class Navigation extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            username: "username",
            isAuthenticated: false
        }
    }

    render() {
        let auth = this.state.isAuthenticated
        return (
            <Router>
                <Navbar bg="light">
                    <Navbar.Brand as={Link} to="/" className="nav-link">Kitchen app</Navbar.Brand>
                    {auth && <Nav.Link as={Link} to="/user-ingredients">My ingredients</Nav.Link>}

                    {!auth && <Nav.Link as={Link} to="/register" className="nav-link ml-auto">Register</Nav.Link>}
                    {!auth && <Nav.Link as={Link} to="/login">Login</Nav.Link>}
                    {auth && <Nav.Link as={Link} to="/logout" className="nav-link ml-auto">Logout</Nav.Link>}

                </Navbar>
                <Switch>
                    <Route exact path="/" component={() => <Home isAuthenticated={this.state.isAuthenticated} username ={this.state.username}/>} />
                    <PublicRoute path="/register" isAuthenticated={this.state.isAuthenticated} component={() => <Register />}/>
                    <PublicRoute path="/login" isAuthenticated={this.state.isAuthenticated} component={() => <Login />}/>
                    <PrivateRoute path="/user-ingredients" isAuthenticated={this.state.isAuthenticated} component={() => <UserStock />} />
                </Switch>
            </Router>
        );
    }
}

export default Navigation;
