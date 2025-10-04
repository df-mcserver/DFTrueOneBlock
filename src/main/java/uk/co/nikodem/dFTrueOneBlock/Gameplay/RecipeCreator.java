package uk.co.nikodem.dFTrueOneBlock.Gameplay;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import uk.co.nikodem.dFTrueOneBlock.DFTrueOneBlock;

import java.util.*;

import static org.bukkit.Bukkit.getServer;

public abstract class RecipeCreator {
    private final String idTag = "-dfsmprecipes";
    List<Recipe> recipesToAdd = new ArrayList<>();
    static List<NamespacedKey> namespacedKeysToDiscover = new ArrayList<>();

    public final DFTrueOneBlock plugin;

    public RecipeCreator(DFTrueOneBlock plugin) {
        this.plugin = plugin;

        createRecipes(); // add the recipes to the list

        for (Recipe recipe : recipesToAdd) { // add the recipes from the list :)
            Bukkit.addRecipe(recipe);
        }
        recipesToAdd = null;
    }

    protected void createRecipes() {

    }

    public void discoverRecipes(Player plr) { // autodiscovery
        // no auto discovery
        for (NamespacedKey namespacedKey : namespacedKeysToDiscover) { // add the recipes from the list :)
            plr.discoverRecipe(namespacedKey);
        }
    }

    public ShapedRecipe createShapedRecipe(ItemStack result, String extra) {
        return new ShapedRecipe(
                MakeNamespacedKey(
                        MakeId(result, extra)
                ),
                result
        );
    }

    public enum RecipeType {
        SHAPED,
        SHAPELESS,
        ANYCRAFTING,
        ANYFURNACE,
        FURNACE,
        BLASTING,
        SMITHING,
        STONECUTTING
    }

    public void RemoveRecipesWithResult(RecipeType recipeType, Material result) {
        RemoveRecipesWithResults(recipeType, result);
    }

    public void RemoveRecipesWithResults(RecipeType recipeType, Material... results) {
        Iterator<Recipe> it = getServer().recipeIterator();
        Recipe recipe;
        while(it.hasNext()) {
            recipe = it.next();
            if (recipe != null) {
                if ((recipe instanceof BlastingRecipe && recipeType == RecipeType.BLASTING)
                        || (recipe instanceof FurnaceRecipe && recipeType == RecipeType.FURNACE)
                        || (recipe instanceof ShapedRecipe && recipeType == RecipeType.SHAPED)
                        || (recipe instanceof ShapelessRecipe && recipeType == RecipeType.SHAPELESS)
                        || (recipe instanceof SmithingTransformRecipe && recipeType == RecipeType.SMITHING)

                        || (recipe instanceof StonecuttingRecipe && recipeType == RecipeType.STONECUTTING)

                        || ((recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe) && recipeType == RecipeType.ANYCRAFTING)
                        || ((recipe instanceof FurnaceRecipe || recipe instanceof BlastingRecipe) && recipeType == RecipeType.ANYFURNACE)) {
                    for (Material item : results) {
                        if (recipe.getResult().getType() == item) {
                            it.remove();
                            break;
                        }
                    }
                }
            }
        }
    }

    public void ReplaceIngredientWithExactChoice(Material ingredient) {
        Iterator<Recipe> it = getServer().recipeIterator();
        Recipe recipe;
        while(it.hasNext()) {
            recipe = it.next();
            if (recipe != null) {
                if (recipe instanceof ShapedRecipe shaped) {
                    for (var ing : shaped.getIngredientMap().entrySet()) {
                        if (ing.getValue() == null) return;
                        if (ing.getValue().getType() == ingredient) {
                            Character key = ing.getKey();
                            shaped.setIngredient(key, new RecipeChoice.ExactChoice(new ItemStack(ingredient)));
                        }
                    }
                } else if (recipe instanceof ShapelessRecipe shapeless) {
                    for (ItemStack ing : shapeless.getIngredientList()) {
                        if (ing.getType() == ingredient) {
                            shapeless.removeIngredient(ingredient);
                            shapeless.addIngredient(new RecipeChoice.ExactChoice(new ItemStack(ingredient)));
                        }
                    }
                }
            }
        }
    }

    public void RemoveRecipesWithIngredient(RecipeType recipeType, Material ingredient) {
        RemoveRecipesWithIngredients(recipeType, ingredient);
    }

    public void RemoveRecipesWithIngredients(RecipeType recipeType, Material... ingredients) {
        Iterator<Recipe> it = getServer().recipeIterator();
        Recipe recipe;
        while(it.hasNext()) {
            recipe = it.next();
            if (recipe != null) {
                if (
                        (recipe instanceof ShapedRecipe && recipeType == RecipeType.SHAPED)
                                || (recipe instanceof ShapedRecipe && recipeType == RecipeType.ANYCRAFTING)) {
                    ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
                    for (Map.Entry<Character, ItemStack> ing : shapedRecipe.getIngredientMap().entrySet()) {
                        for (var ingredient : ingredients) {
                            if (ing.getValue().getType() == ingredient) {
                                it.remove();
                                break;
                            }
                        }
                    }
                } else if ((recipe instanceof ShapelessRecipe && recipeType == RecipeType.SHAPELESS)
                        || (recipe instanceof ShapelessRecipe && recipeType == RecipeType.ANYCRAFTING)) {
                    ShapelessRecipe shapelessRecipe = (ShapelessRecipe) recipe;
                    for (ItemStack ing : shapelessRecipe.getIngredientList()) {
                        for (var ingredient : ingredients) {
                            if (ing.getType() == ingredient) {
                                it.remove();
                                break;
                            }
                        }
                    }
                } else if (
                        (recipe instanceof FurnaceRecipe && recipeType == RecipeType.FURNACE)
                                || (recipe instanceof FurnaceRecipe && recipeType == RecipeType.ANYFURNACE)
                ) {
                    FurnaceRecipe furnaceRecipe = (FurnaceRecipe) recipe;
                    for (var ingredient : ingredients) {
                        if (furnaceRecipe.getInput().getType() == ingredient) {
                            it.remove();
                            break;
                        }
                    }
                } else if ((recipe instanceof BlastingRecipe && recipeType == RecipeType.BLASTING)
                        || (recipe instanceof BlastingRecipe && recipeType == RecipeType.ANYFURNACE)
                ) {
                    BlastingRecipe blastingRecipe = (BlastingRecipe) recipe;
                    for (var ingredient : ingredients) {
                        if (blastingRecipe.getInput().getType() == ingredient) {
                            it.remove();
                            break;
                        }
                    }
                } else if (recipe instanceof SmithingTransformRecipe smithingTransformRecipe && recipeType == RecipeType.SMITHING) {
                    for (var ingredient : ingredients) {
                        if (Objects.equals(smithingTransformRecipe.getBase(), new RecipeChoice.ExactChoice(new ItemStack(ingredient)))) {
                            it.remove();
                            break;
                        }
                    }
                } else if (recipe instanceof StonecuttingRecipe stonecuttingRecipe && recipeType == RecipeType.STONECUTTING) {
                    for (var ingredient : ingredients) {
                        if (stonecuttingRecipe.getInput().getType() == ingredient) {
                            it.remove();
                            break;
                        }
                    }
                }
            }
        }
    }

    public ShapedRecipe createShapedRecipe(ItemStack result) {
        return createShapedRecipe(result, "");
    }

    public ShapedRecipe createShapedRecipe(Material result, int amount, String extra) {
        return createShapedRecipe(new ItemStack(result, amount), extra);
    }

    public ShapedRecipe createShapedRecipe(Material result, int amount) {
        return createShapedRecipe(new ItemStack(result, amount), "");
    }

    public ShapedRecipe createShapedRecipe(Material result, String extra) {
        return createShapedRecipe(new ItemStack(result), extra);
    }

    public ShapedRecipe createShapedRecipe(Material result) {
        return createShapedRecipe(new ItemStack(result), "");
    }

    public ShapelessRecipe createShapelessRecipe(ItemStack result, String extra) {
        return new ShapelessRecipe(
                MakeNamespacedKey(
                        MakeId(result, extra)
                ),
                result
        );
    }

    public ShapelessRecipe createShapelessRecipe(ItemStack result) {
        return createShapelessRecipe(result, "");
    }

    public ShapelessRecipe createShapelessRecipe(Material result, int amount, String extra) {
        return createShapelessRecipe(new ItemStack(result, amount), extra);
    }

    public ShapelessRecipe createShapelessRecipe(Material result, int amount) {
        return createShapelessRecipe(new ItemStack(result, amount), "");
    }

    public ShapelessRecipe createShapelessRecipe(Material result) {
        return createShapelessRecipe(new ItemStack(result), "");
    }

    public ShapelessRecipe createShapelessRecipe(Material result, String extra) {
        return createShapelessRecipe(new ItemStack(result), extra);
    }

    public FurnaceRecipe createFurnaceRecipe(Material source, ItemStack result, Float exp, int time, String extra) {
        return new FurnaceRecipe(
                MakeNamespacedKey(
                        MakeId(result, extra)
                ),
                result,
                source,
                exp,
                time
        );
    }

    public FurnaceRecipe createFurnaceRecipe(Material source, Material result, Float exp, int time, String extra) {
        return createFurnaceRecipe(source, new ItemStack(result), exp, time, extra);
    }

    public FurnaceRecipe createFurnaceRecipe(Material source, ItemStack result, String extra) {
        return createFurnaceRecipe(source, new ItemStack(result), 1F, 100, extra);
    }

    public FurnaceRecipe createFurnaceRecipe(Material source, Material result, Float exp, int time) {
        return createFurnaceRecipe(source, new ItemStack(result), exp, time, "");
    }

    public FurnaceRecipe createFurnaceRecipe(Material source, ItemStack result) {
        return createFurnaceRecipe(source, result, 1F, 100, "");
    }

    public FurnaceRecipe createFurnaceRecipe(Material source, Material result) {
        return createFurnaceRecipe(source, new ItemStack(result), 1F, 100, "");
    }

    public FurnaceRecipe createFurnaceRecipe(RecipeChoice.ExactChoice source, ItemStack result, Float exp, int time, String extra) {
        return new FurnaceRecipe(
                MakeNamespacedKey(
                        MakeId(result, extra)
                ),
                result,
                source,
                exp,
                time
        );
    }

    public FurnaceRecipe createFurnaceRecipe(RecipeChoice.ExactChoice source, ItemStack result) {
        return createFurnaceRecipe(source, result, 1F, 100, "");
    }

    public FurnaceRecipe createFurnaceRecipe(RecipeChoice.ExactChoice source, ItemStack result, Float exp) {
        return createFurnaceRecipe(source, result, exp, 100, "");
    }

    public FurnaceRecipe createFurnaceRecipe(RecipeChoice.ExactChoice source, ItemStack result, Float exp, int time) {
        return createFurnaceRecipe(source, result, exp, time, "");
    }

    public BlastingRecipe createBlastingRecipe(Material source, ItemStack result, Float exp, int time, String extra) {
        return new BlastingRecipe(
                MakeNamespacedKey(
                        MakeId(result, extra)
                ),
                result,
                source,
                exp,
                time
        );
    }

    public BlastingRecipe createBlastingRecipe(Material source, Material result, Float exp, int time, String extra) {
        return createBlastingRecipe(source, new ItemStack(result), exp, time, extra);
    }

    public BlastingRecipe createBlastingRecipe(Material source, ItemStack result, String extra) {
        return createBlastingRecipe(source, new ItemStack(result), 1F, 100, extra);
    }

    public BlastingRecipe createBlastingRecipe(Material source, Material result, Float exp, int time) {
        return createBlastingRecipe(source, new ItemStack(result), exp, time, "");
    }

    public BlastingRecipe createBlastingRecipe(Material source, ItemStack result) {
        return createBlastingRecipe(source, result, 1F, 100, "");
    }

    public BlastingRecipe createBlastingRecipe(Material source, Material result) {
        return createBlastingRecipe(source, new ItemStack(result), 1F, 100, "");
    }

    public BlastingRecipe createBlastingRecipe(RecipeChoice.ExactChoice source, ItemStack result, Float exp, int time, String extra) {
        return new BlastingRecipe(
                MakeNamespacedKey(
                        MakeId(result, extra)
                ),
                result,
                source,
                exp,
                time
        );
    }

    public BlastingRecipe createBlastingRecipe(RecipeChoice.ExactChoice source, ItemStack result) {
        return createBlastingRecipe(source, result, 1F, 100, "");
    }

    public BlastingRecipe createBlastingRecipe(RecipeChoice.ExactChoice source, ItemStack result, Float exp) {
        return createBlastingRecipe(source, result, exp, 100, "");
    }

    public BlastingRecipe createBlastingRecipe(RecipeChoice.ExactChoice source, ItemStack result, Float exp, int time) {
        return createBlastingRecipe(source, result, exp, time, "");
    }

    public SmithingTransformRecipe createSmithingTransformRecipe(Material source, ItemStack result, RecipeChoice template, RecipeChoice base, RecipeChoice addition, String extra) {
        return new SmithingTransformRecipe(
                MakeNamespacedKey(
                        MakeId(result, extra)
                ),
                result,
                template,
                base,
                addition
        );
    }

    public SmithingTransformRecipe createSmithingTransformRecipe(Material source, ItemStack result, RecipeChoice template, RecipeChoice base, RecipeChoice addition) {
        return createSmithingTransformRecipe(source, result, template, base, addition, "");
    }

    public SmithingTransformRecipe createSmithingTransformRecipe(RecipeChoice.ExactChoice source, ItemStack result, RecipeChoice template, RecipeChoice base, RecipeChoice addition, String extra) {
        return new SmithingTransformRecipe(
                MakeNamespacedKey(
                        MakeId(result, extra)
                ),
                result,
                template,
                base,
                addition
        );
    }

    public SmithingTransformRecipe createSmithingTransformRecipe(RecipeChoice.ExactChoice source, ItemStack result, RecipeChoice template, RecipeChoice base, RecipeChoice addition) {
        return createSmithingTransformRecipe(source, result, template, base, addition, "");
    }

    public StonecuttingRecipe createStonecuttingRecipe(Material source, ItemStack result, String extra) {
        return new StonecuttingRecipe(
                MakeNamespacedKey(
                        MakeId(result, extra)
                ),
                result,
                source
        );
    }

    public StonecuttingRecipe createStonecuttingRecipe(Material source, ItemStack result) {
        return createStonecuttingRecipe(source, result, "");
    }

    public StonecuttingRecipe createStonecuttingRecipe(RecipeChoice.ExactChoice source, ItemStack result, String extra) {
        return new StonecuttingRecipe(
                MakeNamespacedKey(
                        MakeId(result, extra)
                ),
                result,
                source
        );
    }

    public StonecuttingRecipe createStonecuttingRecipe(RecipeChoice.ExactChoice source, ItemStack result) {
        return createStonecuttingRecipe(source, result, "");
    }

    public String MakeId(ItemStack item, String extra) {
        return "recipe-"+item.getType().toString().toLowerCase().replace("_", "")+extra+idTag;
    }

    public NamespacedKey MakeNamespacedKey(String id) {
        NamespacedKey key = new NamespacedKey(plugin, id);
        namespacedKeysToDiscover.add(key);
        return key;
    }

    public void RegisterRecipe(Recipe recipe) {
        recipesToAdd.add(recipe);
    }

    public void RegisterRecipe(ShapedRecipe recipe) {
        RegisterRecipe((Recipe) recipe);
    }

    public void RegisterRecipe(ShapelessRecipe recipe) {
        RegisterRecipe((Recipe) recipe);
    }

    public void RegisterRecipe(FurnaceRecipe recipe) {
        RegisterRecipe((Recipe) recipe);
    }

    public void RegisterRecipe(SmithingTransformRecipe recipe) {
        RegisterRecipe((Recipe) recipe);
    }

    public void RegisterRecipe(BlastingRecipe recipe) {
        RegisterRecipe((Recipe) recipe);
    }

    public void RegisterRecipe(StonecuttingRecipe recipe) {
        RegisterRecipe((Recipe) recipe);
    }
}
