import React, {Component} from "react";
import {Alert, Button, Form} from "react-bootstrap";
import axios from "axios";

class Login extends Component {

    constructor(props) {
        super(props);
        this.state = {
            username: "",
            password: "",
            success: null
        }
        this.handleChange = this.handleChange.bind(this)
        this.handleSubmit = this.handleSubmit.bind(this)
    }

    handleChange(e) {
        this.setState({[e.target.name] : e.target.value})
    }

    handleSubmit(e) {
        e.preventDefault()
        let toSave = {
            username: this.state.username,
            email: this.state.email,
            password: this.state.password
        }
        axios.post('/user', toSave, {
            "Content-Type":"application/json"
        })
            .then(res => this.setState({success: true})
            )
            .catch(error => this.setState({success: false}));
    }

    render() {
        let error
        if(this.state.success!= null && !this.state.success) {
            error = <Alert variant={"danger"}>Login failed. Please check your details again</Alert>
        }
        return(
            <div className="Login">
                {error}
                <h2>Login</h2>
                <Form onSubmit={this.handleSubmit}>
                    <Form.Group controlId="formUsername">
                        <Form.Label>Username</Form.Label>
                        <Form.Control name="username" type="text" value={this.state.username} onChange={this.handleChange}/>
                    </Form.Group>

                    <Form.Group controlId="formPassword">
                        <Form.Label>Password</Form.Label>
                        <Form.Control name="password" type="password" value={this.state.password} onChange={this.handleChange}/>
                    </Form.Group>

                    <Button variant="primary" type="submit">
                        Submit
                    </Button>
                </Form>
            </div>
        )
    }
}

export default Login;