import React, {Component} from "react";

import './App.css';
import Navigation from "../components/Navigation/Navigation";
import validateToken from "../api/token-valid";

class App extends Component {

    state = {
        isAuthenticated: localStorage.getItem("user") != null
    }

    componentDidMount() {
        validateToken();
    }

    render() {
        return(
            <div className="App">
                <Navigation isAuthenticated={this.state.isAuthenticated}/>
                <footer>
                    <small>
                        <p>
                            Icons by <a href="https://fontawesome.com/"> FontAwesome</a> and
                            <a href="https://www.flaticon.com/authors/eucalyp" title="Eucalyp"> Eucalyp</a>,
                            <a href="https://www.freepik.com" title="Freepik"> Freepik</a>,
                            <a href="https://www.flaticon.com/authors/iconixar" title="iconixar"> iconixar </a>
                            from <a href="https://www.flaticon.com/" title="Flaticon"> www.flaticon.com</a>
                        </p>

                    </small>
                </footer>
            </div>
        );
    }
}

export default App;
