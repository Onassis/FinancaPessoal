package br.com.fenix.abstrato;

public class GenericInstace<T> {

		private Class<T> classe;
		
		public GenericInstace(Class<T> classe) {
	        this.classe = classe;
	    }
	  
	    public  T createInstance()  {
	          try {
		    	 return classe.getDeclaredConstructor().newInstance();
	          } catch (Exception e) {
	        	  System.out.println(e);
	          }
          return null;
	    }
}
