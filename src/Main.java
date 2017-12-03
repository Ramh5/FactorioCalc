import java.util.ArrayList;
import java.util.Arrays;

public class Main {

	public static void main(String[] args) {

		
		//create items

		Item copperOre = new Item("copperOre", 1);
		Item copperIngot = new Item("copperIngot", 1);
		Item moltenCopper = new Item("moltenCopper", 1);
		Item copperPlate = new Item("copperPlate", 1);
		
		//create a couple unlocked recipes
		ArrayList<Item> ingredients = new ArrayList<Item>();
		ingredients.add(new Item("copperOre", 24));
		ArrayList<Item> result = new ArrayList<Item>(Arrays.asList(new Item("copperIngot",24)));
		Recipe copperIngotR = new Recipe("copperIngot", true, ingredients, 4, "blast smelting", result);
		
		System.out.println(copperIngotR);
		
		ingredients.set(0, new Item("copperIngot",12));
		result.set(0, new Item("moltenCopper", 120));
		Recipe moltenCopperR = new Recipe("moltenCopper", true, ingredients, 4, "induction smelting", result);
		
		System.out.println(copperIngotR);
		
		ingredients.set(0, new Item("moltenCopper", 40 ));
		result.set(0, new Item("copperPlate", 4));
		Recipe copperPlateR = new Recipe("copperPlate", true, ingredients, 4, "casting", result);
		
		System.out.println(copperIngotR);
		
		//create machines
		Machine inductionFurnaceMK1 = new Machine("inductionFurnaceMK1", 150, 1);
		Machine castingMachineMK1 = new Machine("castingMachineMK1", 150, 1);
		Machine blastFurnaceMK1 = new Machine("blastFurnaceMK1", 150, 1);
		
		//create steps
		Step step1 = new Step(new Item("copperPlate", 24), copperPlateR, castingMachineMK1);
		System.out.println(step1);
		Step step2 = new Step(new Item("moltenCopper", 240), moltenCopperR, inductionFurnaceMK1); //TODO have the program figure out the number of items for each steps
		System.out.println(step2);
		Step step3 = new Step(new Item("copperIngot", 24), copperIngotR, blastFurnaceMK1);
		System.out.println(step3);
		ArrayList<Step> steps = new ArrayList<Step>();
		
		steps.add(step1);
		steps.add(step2);
		steps.add(step3);

		
		Chain chain = new Chain(new Item("copperPate", 24), steps);
		
		System.out.println(chain.getEnergyCost()/1000 + " MJ"); //TODO bugged should give 5.4 MJ?
	}

}

abstract class Prototype{
	private String name;
	private String icon;
	
	public Prototype(String name){
		this.name = name;
		this.icon = "icon path"; //TODO
	}
	
	@Override
	public String toString(){
		return " Name: "+ name;
	}
	
}

class Technology extends Prototype{

	private boolean completed;
	private ArrayList<Item> ingredients;
	private double energy;
	private ArrayList<Technology> prereqs;
	private ArrayList<Recipe> effects;
	
	public Technology(String name, boolean completed, ArrayList<Item> ingredients, double energy, ArrayList<Technology> prereqs, ArrayList<Recipe> effects) {
		super(name);
		this.completed = completed;
		this.ingredients = new ArrayList<Item>(ingredients);
		this.energy = energy;
		this.prereqs = new ArrayList<Technology>(prereqs);
		this.effects = new ArrayList<Recipe>(effects); // TODO unlock recipes
	}
	

	
}

class Recipe extends Prototype{
	
	private boolean unlocked;
	private ArrayList<Item> ingredients;
	private double energy;
	private String craftingCat;
	private ArrayList<Item> result;
	
	public Recipe(String name, boolean unlocked, ArrayList<Item> ingredients, double energy, String craftingCat, ArrayList<Item> result) {
		super(name);
		this.unlocked = unlocked;
		this.ingredients = new ArrayList<Item>(ingredients);
		this.energy = energy;
		this.craftingCat = craftingCat;
		this.result = new ArrayList<Item>(result);
	}
	
	public double getEnergy(){
		return energy;
	}
	
	public double getResultAmount(){
		return result.get(0).getAmount(); //TODO only work for one item result recipe for now
	}
	
	@Override
	public String toString(){
		return super.toString() + "   energy:  " + energy + " s  category: " + craftingCat + " item per: " + result.get(0).getAmount() ; //TODO only support one type of item per recipe
	}
	
}

class Machine extends Prototype{
	private double power;
	private double speed;
	
	public Machine(String name, double power, double speed) {
		super(name);
		this.power = power;
		this.speed = speed;
	}
	
	public double getPower(){
		return power;
	}
	
	public double getSpeed(){
		return speed;
	}
	
}

class Item extends Prototype{

	private double amount;
	
	public Item(String name, double amount) {
		super(name);
		this.amount = amount;
	}
	
	public double getAmount(){
		return amount;
	}
}

abstract class Work{
	private Item want;
	private double energyCost;
	private double timeCost;
	private ArrayList<Item> itemCost;
	private ArrayList<Item> produced;
	
	public Work(Item want){
		this.want = want;
	}
	
	public void setEnergyCost(double energyCost){
		this.energyCost = energyCost;
	}
	
	public double getEnergyCost(){
		return energyCost;
	}
}


class Step extends Work{
	private Recipe recipe;
	private Machine machine;
	
	public Step(Item want, Recipe recipe, Machine machine){
		super(want);
		this.recipe = recipe;
		this.machine = machine;
		
		super.setEnergyCost(( machine.getPower() * want.getAmount() * recipe.getEnergy() ) / ( machine.getSpeed() * recipe.getResultAmount() ));
		//TODO initialise other attributes
	}
	
	public String toString(){
		return "[Recipe] " + recipe.toString() + " 	[Machine] " + machine.toString();
	}
}

class Chain extends Work{
	private ArrayList<Step> steps;
	
	public Chain(Item want, ArrayList<Step> steps){
		super(want);
		this.steps = new ArrayList<Step>(steps);
		
		super.setEnergyCost(steps.get(0).getEnergyCost() + steps.get(1).getEnergyCost() + steps.get(2).getEnergyCost()); //TODO only work for size 3 steps
	}
	
}
