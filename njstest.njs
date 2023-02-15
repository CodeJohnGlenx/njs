#LexicalAnalyzer
'''BSCS 3-4
Principle of Programming Languages
(NotJS)'''

float bill(float addcost, float pzwotops){
    return addcost+pzwotops;
}

void main(){
    String tops[] = "1. Pepperoni" , "2. Pineapple", "3. Cheese", "4. Mushroom", "5. Onion", "6. Exit"
    final float pzwotops = 100.00;
    int addcost=0;
    tops_index[];
    output("PIZZA ORDERING SYSTEM IN NOTJS");
    output("---SELECT YOUR TOPPINGS----");
    for(int i = 0; i<tops; i++){
        output(tops[i])
    }
    
    int tops_num= input("\n\nHow many toppings do you want: ");
    output("\nSelect your toppings");
    int tops_list[]; 
    
    for(int i=0; i<tops_num; i++){
        input(tops_list[i]);
    }

    do{
        if(tops_list[i] == 0){
            addcost = addcost+15.20;
            tops_index += tops_list[i]
        }
        elif(tops_list[i] == 1){
            addcost = addcost+20.50;
            tops_index += tops_list[i]
        }
        eliftops_list[i] == 2){
            addcost = addcost+12.10;
            tops_index += tops_list[i]
        }
        elif(tops_list[i] == 3){
            addcost = addcost+22.50;
            tops_index += tops_list[i]
        }
        elif(tops_list[i] == 4){
            addcost = addcost+50.10;
            tops_index += tops_list[i]
        }
        elif(tops_list[i] == 5){
            output("Thank you!");
            break;
        }
        else
        output("ERROR: Invalid input. Try again!");
    }for(int i=0; i<tops_num; i++);

    ordered_tops[] = for(int i = 0; i < tops_index; i++): ordered_tops += tops_index[i];

    for(int i = 0;  i < tops_num; i++){
        output(tops[ordered_tops[i]])
    }

    float tot_cost = bill(addcost, pzwotops);
    output("\nYour total is: ",tot_cost);
}

