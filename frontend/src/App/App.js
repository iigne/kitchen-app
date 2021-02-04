import React, {Component} from "react";

import './App.css';
import Navigation from "../components/Navigation/Navigation";

class App extends Component {

    state = {
        isAuthenticated: localStorage.getItem("user") != null
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
