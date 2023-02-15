#LexicalAnalyzer
'''BSCS 3-4
Principle of Programming Languages
(NotJS)'''


void main(){
    final float pzwotops = 100.00;
    float addcost=0;
    
    output("===PIZZA ORDERING SYSTEM IN NOTJS====");
    output("---SELECT YOUR TOPPINGS----");
    output("\n1. Pepperoni");
    output("\n2. Pineapple");
    output("\n3. Cheese");
    output("\n4. Mushroom");
    output("\n5. Onion");
    output("\n6. Exit");

    int tops_num= input("\n\nHow many toppings do you want: ");
    int tops_recount = for (int i = 0; i < tops_num; i++): i+1;
    output("\nSelect your toppings:");

    int tops_list; 
    for(int j=0; i<tops_num; j++){
        tops_list = input("input tops list");
    }    

#using do-for loop
    do{
        if(tops_list== 0){
            addcost = addcost+15.20;
        }
        elif(tops_list== 1){
            addcost = addcost+20.50;
        }
        elif(tops_list== 2){
            addcost = addcost+12.10;
        }
        elif(tops_list== 3){
            addcost = addcost+22.50;
        }
        elif(tops_list== 4){
            addcost = addcost+50.10;
        }
        elif(tops_list== 5){
            output("Thank you!");
            break;
        }
        else
            output("ERROR: Invalid input. Try again!");
    }for(int k=0; i<tops_num; k++);

    float tot_cost = (addcost + pzwotops);
    output("\nYour total is: ",tot_cost);    
} 




