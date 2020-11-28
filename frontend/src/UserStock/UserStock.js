import React from "react";
import axios from "axios";


class UserStock extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            success: null,
            content: ""
        }
        this.callSecuredEndpoint = this.callSecuredEndpoint.bind(this)
    }

    callSecuredEndpoint() {
        axios.get('/secured-hello',
            {headers: {
                authorization: 'Basic ' + window.btoa("user:hello")
                }})
            .then(res => {
                this.setState({success: true});
                this.setState({content: res.data})
            }
            )
            .catch(error => {
                this.setState({success: false});
                this.setState({content: error.content})
            });
    }

    render() {
        return(
            <div>
                <h1>Hello logged in user this is your ingredients</h1>
                <button onClick={this.callSecuredEndpoint}>hello</button>
                <p>success: {this.state.success ? "yes" : "no"}</p>
                <p>content: {this.state.content}</p>
            </div>

        );
    }
}

export default UserStock;