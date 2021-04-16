import React, {Component} from "react";

import './App.css';
import Navigation from "../components/Navigation/Navigation";
import validateToken from "../api/token-valid";
import {withAlert} from "react-alert";

class App extends Component {

    componentDidMount() {
        validateToken();
    }

    showAlert = (text, type) => {
        if (type === "error"){
            this.props.alert.error(text);
        } else if (type === "success") {
            this.props.alert.success(text);
        } else {
            this.props.alert.show(text);
        }
    }


    render() {
        return (
            <div className="App">
                <Navigation showAlert={this.showAlert}/>
                <hr/>
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

export default withAlert()(App);
