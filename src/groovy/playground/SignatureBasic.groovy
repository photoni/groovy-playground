package groovy.playground

class SignatureBasic {
	static void main(def args) {
		
				
				def sb=new SignatureBasic();
				println sb.twoArgs(2,2)
				println sb.nArgs(1,2,3,4,5,6,7,8,9,0)
			}
	
	int twoArgs(int x,int y){
		return x+y;
		
	}
	/** 
	 * Variable argument closures
	 * @param args
	 * @return
	 */
	int nArgs(int... args){
		int sum=0	
		args.each {i -> sum+=i}
		return sum
		
	}

}
