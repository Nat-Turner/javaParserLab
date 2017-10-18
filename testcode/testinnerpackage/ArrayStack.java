package testcode.testinnerpackage;

public class ArrayStack<E> implements StackADT<E>
{
    int top;
    E[] S;
	int[] smallArray;
    /**
     * Constructor for objects of class ArrayStack
     */
    public ArrayStack(int capacity)
    {
        top = -1;
        S = (E[]) new Object[capacity]; // casting needed
    }
    
    public ArrayStack(){
    	
    	smallArray = new int[3];
    	
    }

   /** 
     @throws FullStackException
   */
   public void push(E element){
     if (size() == S.length-1)
        throw new FullStackException("Stack is full, so cannot push on to stack");
     top++;
     S[top] = element;
   }

   /**
     @throws EmptyStackException
   */
   public E pop(){
     E element;
     if (size() == 0)
        throw new EmptyStackException("Stack is empty, so cannot pop from stack");
     element = S[top];
     top--;
     return element;
   }
    

   public E top(){
	 
	 try{
		 return S[top];
	 } catch (EmptyStackException e){
		 throw new EmptyStackException("Stack is empty, cannot return top value");
	 }	 
   }

   public int size(){
	  int size = top + 1;
      return size;
      
   };

   public boolean isEmpty(){
      if (top == -1){
    	  return true;
      }
      else{
    	  return false;
      }
   }  
}


