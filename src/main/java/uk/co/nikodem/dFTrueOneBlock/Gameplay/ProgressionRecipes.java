package uk.co.nikodem.dFTrueOneBlock.Gameplay;

import org.bukkit.Material;
import uk.co.nikodem.dFTrueOneBlock.DFTrueOneBlock;

public class ProgressionRecipes extends RecipeCreator {

    public ProgressionRecipes(DFTrueOneBlock plugin) {
        super(plugin);
    }

    @Override
    protected void createRecipes() {
        RegisterRecipe(
                createShapelessRecipe(Material.GRASS_BLOCK)
                        .addIngredient(Material.WHEAT_SEEDS)
                        .addIngredient(Material.DIRT)
        );
    }
}
