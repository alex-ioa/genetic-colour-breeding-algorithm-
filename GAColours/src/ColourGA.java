

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class ColourGA implements KeyListener{

	Random RANDOM_VALUE = new Random();
	float MUTATION_RATE = .01f;
	static ArrayList<String> population = new ArrayList<String>();
	static ArrayList<String> newPopulation = new ArrayList<String>();
	static ArrayList<String> selection = new ArrayList<String>();
	JButton crossOver = new JButton();
	JPanel panel = new JPanel();
	JFrame frame = new JFrame("Colour GA");
	static String offSpring1;
	static String offSpring2;
	private static drawPopulation drawPop;
	int generationNumber;
	
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		ColourGA d = new ColourGA(drawPop, false);
	}

	public ColourGA(final drawPopulation drawPop, boolean isNewGen){
		System.out.println("Fresh and number: "+generationNumber);
		
		//The if statement will check to see if this is a new generation, if not then we need to make a population!
		if(isNewGen == false){
		for(int i = 0; i < 8; i++)
		{
			population.add(i, randomBitString(24));
		}
		}
		ColourGA.drawPop = drawPop; 

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new drawPopulation());
		frame.addKeyListener(new KeyListener(){
		    @Override 
		    public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					newGeneration();
					frame.dispose();
					@SuppressWarnings("unused")
					ColourGA d = new ColourGA(drawPop, true);
				}
			}

		    @Override
		    public void keyReleased(KeyEvent e)
		    {

		    }
		              
		    @Override
		    public void keyTyped(KeyEvent e)
		    {
		        
		    }
		});
		frame.setSize(1280,854);
		frame.setVisible(true);  
		

	}


	@SuppressWarnings("serial")
	public class drawPopulation extends JComponent{
		public void paint(Graphics gfx){
			
			int y = 60;

			for(int i = 0; i < 8; i++, y += 90)
			{
				//Calculate the normalised RGB Values
				System.out.println("The index is at "+i);
				System.out.println("Chosen String for population RGB is: "+population.get(i));
				float r = (float)GetRGBValue(population.get(i), 'r') / 255.0f;
				System.out.println("The float value for red is: "+r);
				float g = (float)GetRGBValue(population.get(i), 'g') / 255.0f;
				System.out.println("The float value for green is: "+g);
				float b = (float)GetRGBValue(population.get(i), 'b') / 255.0f;
				System.out.println("The float value for blue is: "+b);

				//Draw the Rectangle
				gfx.setColor(new Color(r, g, b));
				gfx.fillRect(80, y, 165, 60);


				//Show the Fitness value
				int fitval = Fitness(population.get(i));
				String fit = "Fitness of colour "+ i + " is: "+ fitval;
				gfx.setColor(new Color(0, 0, 0));
				gfx.setFont(new Font("TimesRoman", Font.BOLD, 11));
				gfx.drawString(fit, 80, y-1);

				//Display the bit string
				gfx.setColor(new Color(0, 0, 0));
				gfx.drawString(population.get(i), 80, y+71);
				
				
			}	

			System.out.println("Matching...");
			matchPair();
			System.out.println("Matching done...");
			


			y = 60;


			for(int i = 0; i < 8; i++, y += 90)
			{
				//Calculate the normalized RGB Values
				System.out.println("The selection index is at "+i);
				System.out.println("Chosen String for selection is: "+selection.get(i));
				float r = (float)GetRGBValue(selection.get(i), 'r') / 255.0f;
				System.out.println("The float value for red is: "+r);
				float g = (float)GetRGBValue(selection.get(i), 'g') / 255.0f;
				System.out.println("The float value for green is: "+g);
				float b = (float)GetRGBValue(selection.get(i), 'b') / 255.0f;
				System.out.println("The float value for blue is: "+b);

				//Draw the Rectangle
				gfx.setColor(new Color(r, g, b));
				gfx.fillRect(480, y, 165, 60);


				//Show the Fitness value
				int fitval = Fitness(selection.get(i));
				String fit = "Fitness: "+ fitval;
				gfx.setColor(new Color(0, 0, 0));
				gfx.setFont(new Font("TimesRoman", Font.BOLD, 11));
				gfx.drawString(fit, 480, y-1);

				//Display the bit string
				gfx.setColor(new Color(0, 0, 0));
				gfx.drawString(selection.get(i), 480, y+71);	
			}


			for(int i = 0; i < 4; i += 2)
			{
				String temp1 = selection.get(i);
				String temp2 = selection.get(i+1);

				Crossover(temp1, temp2);

				Mutate(offSpring1);
				Mutate(offSpring2);

				System.out.println("New offspring is born!: "+offSpring1);
				System.out.println("New offspring is born!: "+offSpring2);
				newPopulation.add(offSpring1);
				newPopulation.add(offSpring2);


			}
			System.out.println("Old population is: "+population);
			System.out.println("New poulation is:  "+newPopulation);

			
			y = 60;


			for(int i = 0; i < 8; i++, y += 90)
			{
				//Calculate the normalized RGB Values
				System.out.println("The offspring index is at "+i);
				System.out.println("Chosen String for offspring is: "+newPopulation.get(i));
				float r = (float)GetRGBValue(newPopulation.get(i), 'r') / 255.0f;
				System.out.println("The float value for red is: "+r);
				float g = (float)GetRGBValue(newPopulation.get(i), 'g') / 255.0f;
				System.out.println("The float value for green is: "+g);
				float b = (float)GetRGBValue(newPopulation.get(i), 'b') / 255.0f;
				System.out.println("The float value for blue is: "+b);

				//Draw the Rectangle
				gfx.setColor(new Color(r, g, b));
				gfx.fillRect(880, y, 165, 60);


				//Show the Fitness value
				int fitval = Fitness(newPopulation.get(i));
				String fit = "Fitness: "+ fitval;
				gfx.setColor(new Color(0, 0, 0));
				gfx.setFont(new Font("TimesRoman", Font.BOLD, 11));
				gfx.drawString(fit, 880, y-1);

				//Display the bit string
				gfx.setColor(new Color(0, 0, 0));
				gfx.drawString(newPopulation.get(i), 880, y+71);	
			}
			
			//Labels
			gfx.setFont(new Font("TimesRoman", Font.BOLD, 18));
			gfx.drawString("Population", 125, 30);
			gfx.drawString("Paired Colours (Parents)", 470, 30);
			gfx.drawString("Offspring", 925, 30);
		}
		
		

	}


		

	void newGeneration()
	{
		//This starts a new generation, I realised later that my code wouldnt allow for the storage of generationNumber because a new one is called at the start
		//generationNumber++;
		System.out.println("Clearing old population");
		population.clear(); 
		for(int i = 0; i < 8; i++){
				population.add(newPopulation.get(i));	
				System.out.println("Added from new population: "+newPopulation.get(i));
		}
		selection.clear();
		newPopulation.clear(); 
		System.out.println("This is the new population: "+population);
	}
	
	void Crossover(String org1, String org2)
	{
		String temp1 = "";
		String temp2 = "";

		System.out.println("Parent 1 is: "+org1);
		System.out.println("Parent 2 is: "+org2);

		//The first parent is org1 and the second parent is org2, the crossover point is decided in this next line of code
		int crossoverPoint = (int) (RANDOM_VALUE.nextFloat() * 24);
		System.out.println("Crossover point is: "+crossoverPoint);

		//Then we snip the first part of parent 1
		temp1 += org1.substring(0, crossoverPoint);
		System.out.println("Offspring 1 is inheriting from parent 1: "+temp1);
		//Then we snip the remaining bits of parent 2
		temp1 += org2.substring(crossoverPoint, 24);
		System.out.println("Offspring 1s new bit string from both parents: "+temp1);

		temp2 += org2.substring(0, crossoverPoint);
		System.out.println("Offspring 2 is inheriting from parent 1: "+temp2);
		temp2 += org1.substring(crossoverPoint, 24);
		System.out.println("Offspring 2s new bit string from both parents: "+temp2);

		//The new offspring are created...
		offSpring1 = temp1;
		offSpring2 = temp2;
	}

	void Mutate(String org)
	{
		//Each bit has a 100 * MUTATION_RATE % chance to flip
		for(int i = 0; i < org.length(); i++)
		{
			if(RANDOM_VALUE.nextFloat() <= MUTATION_RATE)
			{
				
				if(org.charAt(i) == '1'){
					System.out.println("Mutation of 1 into 0 at position: "+i);
					org.replace(org.charAt(i), '0');
				}else{
					System.out.println("Mutation of 0 into 1 at postion: "+i);
					org.replace(org.charAt(i), '1');
				}
			}
		}
	}


	int GetRGBValue(String org, char value)
	{
		int index = 0;
		System.out.println("Chosen String For RGB is: "+org);
		switch(value)
		{
		case 'r':
		case 'R':
			System.out.println("CASE IS RED: "+org);
			index = 7;
			break;
		case 'g':
		case 'G':
			System.out.println("CASE IS GREEN: "+org);
			index = 15;
			break;
		case 'b':
		case 'B':
			System.out.println("CASE IS BLUE: "+org);
			index = 23;
			break;
		default:
			return 0;		//Value is not r g or b
		}

		int pos_value = 1;
		int ret_value = 0;

		for(int i = index; i > index - 8; i--)
		{
			System.out.println("Index is: "+index);
			System.out.println("String character is: "+org.charAt(i));
			if(org.charAt(i) == '1'){
				System.out.println("So we use: "+org.charAt(i));
				ret_value += pos_value;
				System.out.println("The first ret value is: "+ret_value);
				System.out.println("The first pos value is: "+pos_value);
			}
			pos_value *= 2;
			System.out.println("The new pos value is: "+pos_value);
		}
		System.out.println("The final ret value is: "+ret_value);
		System.out.println("**************************************************");
		System.out.println("");
		return ret_value;
	}

	int Fitness(String org)
	{
		//Set up for the color Magenta (255, 0, 255)
		return GetRGBValue(org, 'r') + 255 - GetRGBValue(org, 'g') + GetRGBValue(org, 'b');
	}


	private static String randomBitString(int length) {
		//Generate a random bit string of length bits
		String bits = "";
		Random randomGenerator = new Random();
		for(int i = 0; i < length; i++)
		{
			//The number generator goes from numbers 0 to 6. There is a 50/50 chance of the bit chosen being a 1 or 0, numbers 0-2 will produce a 0 and numbers 3-5 will produce a 1.
			if(randomGenerator.nextInt(5) > 2)
				bits += "1";
			else
				bits += "0";
		}

		return bits;
	}

	String rouletteWheelSelection() {
		int totalFitness = 0;
		String selectedWeight = null;

		for(int i = 0; i < 8; i++){
			totalFitness += Fitness(population.get(i));
		}

		float randNum = RANDOM_VALUE.nextFloat() * totalFitness;

		for (int i=0; i < 8 && randNum>0; ++i) {
			randNum -= Fitness(population.get(i));
			selectedWeight = (population.get(i));
		}

		return selectedWeight;
	}

	void matchPair()
	{
		//Select the parents that will mate
		for(int i = 0; i < 8; i++){
			selection.add(rouletteWheelSelection());
			System.out.println("Weight added is: "+selection.get(i));
		}
	}


	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {

		
	}

	@Override
	public void keyTyped(KeyEvent e) {

		
	}


}