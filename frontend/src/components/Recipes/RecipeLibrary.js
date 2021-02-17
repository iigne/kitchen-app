import {Button, CardColumns, Container} from "react-bootstrap";
import React from "react";
import RecipeCard from "./RecipeCard";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPlus} from "@fortawesome/free-solid-svg-icons";
import BrowseOption from "./BrowseOption";


class RecipeLibrary extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            currentShow: null,
            recipes: [],
        }
    }

    handleLoadRecipes = (recipes, type) => {
        this.setState({recipes: recipes})
        this.setState({currentShow: type})
    }

    render() {
        let recipes = this.state.recipes
        let type = this.state.currentShow != null ?
            <header className="subheader">Displaying {this.state.currentShow}</header> : null;
        return (
            <Container>

                <header className="header">Recipe library</header>
                <header className="subheader">Create</header>

                <Button variant="outline-success">
                    <FontAwesomeIcon icon={faPlus}/> Create new recipe
                </Button>

                <header className="subheader">Browse</header>
                <Container>
                    <BrowseOption type="suggestion" text="recipe suggestions"
                                  handleLoadRecipes={this.handleLoadRecipes}/>
                    <BrowseOption type="all" text="all recipes" handleLoadRecipes={this.handleLoadRecipes}/>
                    <BrowseOption type="liked" text="recipes I've liked" handleLoadRecipes={this.handleLoadRecipes}/>
                    <BrowseOption type="created" text="recipes I've created"
                                  handleLoadRecipes={this.handleLoadRecipes}/>
                </Container>

                <Container>
                    {type}
                    <CardColumns>
                        {recipes.map((item) =>
                            <RecipeCard {...item} key={item.id}/>
                        )}
                    </CardColumns>
                </Container>

            </Container>
        )
    }


}

export default RecipeLibrary;