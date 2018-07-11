# NFA-to-DFA-Converter
##Formal Language, Dongguk University


*Example 1
>
>               [ e-NFA ]					        		             					[ DFA ]
>   _____<input text file example>_____		    		  		____<output text file example>_____
>  |							                     |			         |	                      				   |
>  |a b c d       	  // Q	           |			    	   |A B C           // Q			         |
>  |0 1 2 e	        // Sigma           |	    			   |0 1 2           // Sigma		       |
>  |a		        		// start state	   |	    			   |A			         	// start state	   |
>  |d			        	// finish states   |			    	   |A C		      		// finish states   |
>  |a 0 b		        // delta list	     |	  	 ==>	   |A 0 B			      // delta list	     |
>  |a e c					                     |				       |A 1 C					              		   |
>  |b 2 a							                 | 		  		     |B 2 A							                 |
>  |b 2 d						                   |	  		   	   |C 1 C							                 |
>  |c 1 c							                 |		     		   |___________________________________|
>  |c e d							                 |
>  |___________________________________|
>  
  

*Example 2

  		        	[ NFA ]												                    [ DFA ]
   _____<input text file example>_____			 	        ____<output text file example>_____
  |a b c d e      	// Q			        |				       |								                   |
  |0 1 		         	// Sigma		      |			    	   |A B C D           // Q			       |
  |a		         		// start state	  |				       |0 1 	          	// Sigma		     |
  |a e			       	// finish states  |				       |A			        	  // start state	 |
  |a 0 b	          // delta list	    |				       |A C D		         	// finish states |
  |a 0 c						                  |		   ==>	   |A 0 B			        // delta list	   |
  |b 1 a  							              |			    	   |B 0 C			              				   |
  |b 1 d  					                  |				       |B 1 D			              				   |
  |c 0 e  						           	    |	    			   |C 0 C			              				   |
  |d 1 e  							              |			    	   |C 1 C					              		   |
  |e 0 e  							              |				       |D 0 B							                 |
  |e 1 e  							              |				       |D 1 C					              		   |
  |___________________________________|				       |___________________________________|
  
