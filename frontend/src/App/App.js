import React, {Component} from "react";

import Register from "../Register/Register";
import './App.css';
import { BrowserRouter as Router, Switch, Route, Link} from 'react-router-dom';
import Login from "../Login/Login";
import Navigation from "../Navigation/Navigation";

class App extends Component {

    state = {
        isAuthenticated: false
    }

    render() {
        return(
            <div className="App">
                <Navigation isAuthenticated={this.state.isAuthenticated}/>
            </div>
        );
    }
}

export default App;
