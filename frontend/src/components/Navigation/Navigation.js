import {Nav, Navbar} from "react-bootstrap";
import {Link, MemoryRouter, Route, Switch} from "react-router-dom";
import Home from "../Home/Home";
import Register from "../Auth/Register";
import Login from "../Auth/Login";
import React from "react";
import PrivateRoute from "./PrivateRoute";
import UserStock from "../UserStock/UserStock";
import PublicRoute from "./PublicRoute";
import Logout from "../Auth/Logout";
import logo from './images/logo-192x192.png'
import fridge from './images/fridge.png'
import recipes from './images/recipe-book.png'
import shopping from './images/grocery-cart.png'
import RecipeLibrary from "../Recipes/RecipeLibrary";
import ShoppingList from "../ShoppingList/ShoppingList";

class Navigation extends React.Component {

    constructor(props) {
        super(props);
        const authJson = localStorage.getItem("user");
        const auth = authJson != null ? JSON.parse(authJson) : null;
        this.state = {
            isAuthenticated: this.isTokenValid(authJson),
            username: auth != null ? auth.username : null,
            userId: auth != null ? auth.id : null
        }
    }

    isTokenValid(authJson) {
        if (authJson != null) {
            const auth = JSON.parse(authJson);
            const timestamp = auth.expiry - 3600;
            const expiry = new Date(timestamp * 1000);
            return new Date() <= expiry;
        }
        return false;
    }

    handleLogin = (auth) => {
        localStorage.setItem("user", JSON.stringify(auth))
        this.setState({
            isAuthenticated: true,
            username: auth.username,
            userId: auth.userId
        })
    }

    handleLogout = () => {
        localStorage.removeItem("user");
        this.setState({isAuthenticated: false})
    }

    render() {
        let auth = this.state.isAuthenticated
        return (
            <MemoryRouter>
                <Navbar bg="light" expand="lg" className="navigation">
                    <Navbar.Brand as={Link} to="/" className="nav-link">
                        <img src={logo} width="30" height="30" alt="App logo - recipe book icon"/>
                    </Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav"/>
                    <Navbar.Collapse id="basic-navbar-nav">
                        {auth && <Nav.Link as={Link} to="/user-ingredients">
                            <img src={fridge} width="30" height="30" alt="Fridge icon"/> My ingredients
                        </Nav.Link>}
                        {auth && <Nav.Link as={Link} to="/recipes">
                            <img src={recipes} width="30" height="30" alt="RecipeCard book icon"/> Recipes
                        </Nav.Link>}
                        {auth && <Nav.Link as={Link} to="/shopping-list">
                            <img src={shopping} width="30" height="30" alt="Shopping cart icon"/>
                            Shopping list
                        </Nav.Link>}
                        {/*{auth && <Nav.Link as={Link} to="/meal-plan">*/}
                        {/*    <img src={calendar} width="30" height="30" alt="Calendar icon"/>*/}
                        {/*    Meal plan</Nav.Link>}*/}

                        {!auth && <Nav.Link as={Link} to="/register" className="nav-link ml-auto">Register</Nav.Link>}
                        {!auth && <Nav.Link as={Link} to="/login">Login</Nav.Link>}
                        {auth && <Nav.Link as={Link} to="/logout" className="nav-link ml-auto">Logout</Nav.Link>}
                    </Navbar.Collapse>


                </Navbar>
                <Switch>
                    <Route exact path="/" component={() => <Home isAuthenticated={this.state.isAuthenticated}
                                                                 username={this.state.username}/>}/>
                    <PublicRoute path="/register" isAuthenticated={this.state.isAuthenticated}
                                 component={() => <Register showAlert={this.props.showAlert}/>}/>
                    <PublicRoute path="/login" isAuthenticated={this.state.isAuthenticated}
                                 component={() => <Login showAlert={this.props.showAlert}
                                                         handleLogin={this.handleLogin}/>}/>
                    <PrivateRoute path="/user-ingredients" isAuthenticated={this.state.isAuthenticated}
                                  component={() => <UserStock showAlert={this.props.showAlert}/>}/>
                    <PrivateRoute path="/recipes" isAuthenticated={this.state.isAuthenticated}
                                  component={() => <RecipeLibrary showAlert={this.props.showAlert}
                                                                  userId={this.state.userId}/>}/>
                    <PrivateRoute path="/shopping-list" isAuthenticated={this.state.isAuthenticated}
                                  component={() => <ShoppingList showAlert={this.props.showAlert}
                                                                 userId={this.state.userId}/>}/>
                    <PrivateRoute path="/logout" isAuthenticated={this.state.isAuthenticated}
                                  component={() => <Logout handleLogout={this.handleLogout}/>}/>
                </Switch>
            </MemoryRouter>
        );
    }
}

export default Navigation;
