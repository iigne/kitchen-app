import React from "react";

import './Ingredient.css';
import {Container} from "react-bootstrap";
import CreateIngredient from "./CreateIngredient";
import Search from "./Search";
import SearchIngredient from "./SearchIngredient";


class AddIngredient extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            selectedIngredient: null,
            selectedMeasurement: null,
            inCreateIngredient: false
        }
    }

    handleSearch = () => {
        this.setState({
            selectedIngredient: null,
            selectedMeasurement: null
        });
    }

    handleSearchSelection = (selectedIngredient) => {
        this.setState({
                selectedIngredient: selectedIngredient,
                selectedMeasurement: selectedIngredient.measurements[0]
            }
        );
    }

    handleChange = (e) => {
        this.setState({[e.target.name]: e.target.value});
    }

    handleAddIngredient = (ingredientData) => {
        this.props.addIngredientHandler(ingredientData);
        this.setState({
            selectedIngredient: null,
            selectedMeasurement: null
        })
    }

    enterCreateIngredient = () => {
        this.setState({inCreateIngredient: true});
    }

    handleFinishCreateIngredient = (success, name, status) => {
        if (success) {
            this.setState({
                    inCreateIngredient: false,
                    searchTerm: name
                }
            );
            this.props.showAlert("Ingredient created - you can now search for it", "success");
        } else {
            this.props.showAlert(status === 400 ?
                "Creation failed - some fields have invalid values" : "Failed to create ingredient",
                "error");
        }
    }

    cancelCreate = () => {
        this.setState({inCreateIngredient: false});
    }

    render() {
        let inCreateIngredient = this.state.inCreateIngredient;
        const selectedIngredient = this.state.selectedIngredient;
        return (
            <Container>
                <Search handleSearch={this.handleSearch}
                        handleSearchSelection={this.handleSearchSelection}
                        enterCreateIngredient={this.enterCreateIngredient}/>

                {selectedIngredient != null &&
                <SearchIngredient {...selectedIngredient}
                                  selectedMeasurement={this.state.selectedMeasurement}
                                  addIngredientHandler={this.handleAddIngredient}/>
                }

                <CreateIngredient show={inCreateIngredient}
                                  handleFinishCreateIngredient={this.handleFinishCreateIngredient}
                                  cancelCreate={this.cancelCreate}/>

            </Container>
        )
    }

}

export default AddIngredient;