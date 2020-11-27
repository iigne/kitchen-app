import React, {Component} from "react";

import './App.css';
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
