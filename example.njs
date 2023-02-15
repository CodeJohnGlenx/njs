void main() {
    # while loop
    int age;
    while (age = input("enter your age") <= 0) {
        output("please enter an appropriate age ", age, "!");
    }


    # if statement
    if (age <= 17) {
        output("you're still a minor");
    } elif (age >= 18 && age < 65) {
        output("you're an adult!");
        String name = input("what's your name? ");

        # switch statement
        switch (name) {
            case "Maria Clara":
                output("WOAH!!!");
                break; 
            case "Juan Dela Cruz":
                output("behold, the place holder name");
                break;
            default:
                output("why hello " + name + "!");
        }
    } elif (age >= 65 && age <= 100) {
        output("hello grandparent!");
    } else {
        output("tell me about dinosaurs!");
    }

    # assignment and arithmetic 
    int num;
    num = input("Enter a number: ");
    
    int reversed = 0;
    while (num != 0) {
        int digit = num % 10;
        reversed = reversed * 10 + digit;
        num /= 10;
    }

    output("Reversed number: " + reversed);    

    # for loop
    int num2;
    for (num2 = 0; num2 <= 100; num += 5) {
        for (int i = 0; i < 10; i++) {
            output(i * (num2 + 3));
        }
    } 

    # do-for
    float fnum = 0;
    do {
        # exp-for
        fnum += for (int j = 0; j < 10; j++) : num + input("add this number: "); 
        output("");
    } for (;num2 > 0; num--);
}