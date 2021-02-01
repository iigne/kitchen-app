import React, {Component} from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCarrot} from "@fortawesome/free-solid-svg-icons";

class Home extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isAuthenticated: props.isAuthenticated,
            username: props.username
        }
    }
    render() {
        let homeMessage
        homeMessage = this.state.isAuthenticated ? "Welcome back, " + this.state.username : "Welcome! Login or register to proceed"
        return(
            <div>
                <header className="App-header">
                    <FontAwesomeIcon icon={faCarrot}/>
                    <p>{homeMessage}</p>
                </header>
            </div>
        );
    }
}

export default Home;

