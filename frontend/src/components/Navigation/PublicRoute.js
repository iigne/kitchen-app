import React from "react";
import {Redirect} from "react-router-dom";

class PublicRoute extends React.Component {

    render() {
        const Component = this.props.component;
        const isAuthenticated = this.props.isAuthenticated;

        return isAuthenticated ?  (<Redirect to={{ pathname: '/' }} />) : (<Component />);
    }
}

export default PublicRoute;