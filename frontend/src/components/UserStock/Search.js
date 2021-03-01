import {Button, Col, Container, Form, Row} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import React from "react";

class Search extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            searchResults: props.searchResults,
            noResults: props.noResults

            // id: props.id,
            // name: props.name,
            // metricUnit: props.metricUnit,
            // measurements: props.measurements
        }
    }

    render() {
        return (
            <div>
                {this.state.searchResults.map((item) =>
                    <Row>
                        <Col xs={1}/>
                        <Col xs={6}>
                            <Button variant="outline-light" block>{item.name}</Button>
                        </Col>
                    </Row>
                )}
                {this.state.noResults === true ?
                    <div>The ingredient you're searching for has not been found. Would you like to create it?</div> : ""}
            </div>
        )

    }
}

export default Search;