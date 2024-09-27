/*
 * Sidney Gills, Zion Tamashiro
 * ICS 311, Fall 2024
 * Assignment 2
 */

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.regex.Pattern;


//Author: Sidney Gills
public class Assignment2 {
    
    //Author: Sidney Gills
    public static class Dictionary {

        /*
         * Author: Sidney Gills
         * Derived from https://www.happycoders.eu/algorithms/red-black-tree-java/
         * Creates a red-black balanced search tree and its root
         * 'key' is the Spanish saying with ASCII characters to allow for sorting.
         * 'saying' is what the user sees, and still contains the characters with diacritics.
         */
        public static class Node {
            String key;
            String saying;
            String english;
            String explanation;
          
            Node left;
            Node right;
            Node parent;
          
            boolean color;
          
            public Node(String key, String saying, String english, String explanation) {
              this.key = key;
              this.saying = saying;
              this.english = english;
              this.explanation = explanation;

              color = RED;
            }
        }
        static final boolean RED = false;
        static final boolean BLACK = true;
        static Node root;

        /*
         * Author: Sidney Gills
         * Derived from https://stackoverflow.com/questions/4122170/java-change-%C3%A1%C3%A9%C5%91%C5%B1%C3%BA-to-aeouu
         * Uses regex to replace diacritic characters with their basic ASCII variant.
         */
        public static String replaceDiacritics(String saying) {
            String asciiSaying = Normalizer
                                    .normalize(saying, Normalizer.Form.NFD)
                                    .replaceAll("[^\\p{ASCII}]", "");
        
            return asciiSaying;
        }

        /*
         * Author: Sidney Gills
         * Derived from https://stackoverflow.com/questions/4067809/how-can-i-find-whitespace-in-a-string
         * The regex pattern is applied to the word to determine if any whitespace characters are present in the string.
         * Returns true if it does not, verifying string consists of a single word.
         */
        public boolean isOneWord(String word) {
            return !Pattern.compile("\\s").matcher(word).find();
        }

        /*
         * Author: Sidney Gills
         * Derived from https://stackoverflow.com/questions/5091057/how-to-find-a-whole-word-in-a-string-in-java
         * The regex pattern is applied to determine if the exact word is present in the string.
         * Returns true if it is, verifying it is not a substring of a bigger word.
         */
        public boolean hasMyWord(String phrase, String word) {
            return Pattern.compile("\\b"+word+"\\b").matcher(phrase).find();
        }
                
        /*
         * Author: Sidney Gills
		 * Derived from CLRS Ch13.2 LEFT-ROTATE(T,x)
		 * Helper function for insertFix(node)
         */
		private static void leftRotate(Node x) {
			Node y = x.right;
			x.right = y.left;
			if (y.left != null) {
                y.left.parent = x;
			}
			
			y.parent = x.parent;
			if (x.parent == null) {
				root = y;
			} else if (x == x.parent.left) {
				x.parent.left = y;
			} else {
				x.parent.right = y;
			}
			
			y.left = x;
			x.parent = y;
		}
		
        /*
		 * Author: Sidney Gills
		 * Derived from CLRS Ch13.2 LEFT-ROTATE(T,x) and symmetrical to leftRotate(Node x)
		 * Helper function for insertFix(node)
         */
		private static void rightRotate(Node x) {
			Node y = x.left;
			x.left = y.right;
			if (y.right != null) {
				y.right.parent = x;
			}

			y.parent = x.parent;
			if (x.parent == null) {
				root = y;
			} else if (x == x.parent.right) {
				x.parent.right = y;
			} else {
				x.parent.left = y;
			}
			
			y.right = x;
			x.parent = y;
		}

        /*
         * Author: Sidney Gills
         * Derived from CLRS Ch13.3 RB-INSERT-FIXUP(T,z)
         * Helper function for insert(saying)
         */
        private static void insertFix(Node z) {
            Node y;
            while (z.parent != null && z.parent.color == RED) {
                if (z.parent.parent != null && z.parent == z.parent.parent.left) {
                    y = z.parent.parent.right;
                    if (y != null && y.color == RED) {
                        z.parent.color = BLACK;
                        y.color = BLACK;
                        z.parent.parent.color = RED;
                        z = z.parent.parent;
                    } else {
                        if (z == z.parent.right) {
                            z = z.parent;
                            leftRotate(z);
                        }
                        z.parent.color = BLACK;
                        if (z.parent.parent != null) {
                            z.parent.parent.color = RED;
                            rightRotate(z.parent.parent);
                        }
                    }
                } else if (z.parent.parent != null) {
                    y = z.parent.parent.left;
                    if (y != null && y.color == RED) {
                        z.parent.color = BLACK;
                        y.color = BLACK;
                        z.parent.parent.color = RED;
                        z = z.parent.parent;
                    } else {
                        if (z == z.parent.left) {
                            z = z.parent;
                            rightRotate(z);
                        }
                        z.parent.color = BLACK;
                        if (z.parent.parent != null) {
                            z.parent.parent.color = RED;
                            leftRotate(z.parent.parent);
                        }
                    }
                }
            }

            root.color = BLACK;
        }
        
        /*
         * Author: Sidney Gills
		 * Derived from CLRS Ch13.3 RB-INSERT(T,z)
		 * Inserts and sorts sayings into alphabetical order based on its key that is converted to ASCII by replacing diacritic characters.
         */
		public void insert(String saying, String english, String explanation) {
			String asciiKey = replaceDiacritics(saying);
            if (member(asciiKey)) {
                System.out.println("'" + saying + "' is already an entry.");
            } else {
                Node z = new Node(asciiKey, saying, english, explanation);
                Node x = root;
                Node y = null;
                while (x != null) {
                    y = x;
                    if (z.key.compareTo(x.key) < 0) {
                        x = x.left;
                    } else { 
                        x = x.right;
                    }
                }
                
                z.parent = y;
                if (y == null) {
                    root = z;
                } else if (z.key.compareTo(y.key) < 0) {
                    y.left = z;
                } else {
                    y.right = z;
                }
    
                z.left = null;
                z.right = null;
                z.color = RED;
                insertFix(z);                
            }
		}
        
        /*
         * Author: Sidney Gills
		 * Derived from CLRS Ch12.2 TREE-SEARCH(x,k)
         * Searches the tree to see if a saying is already present in the dictionary, returning true if it is.
         */
		public boolean member(String asciiKey) {
			Node entry = root;
			while (entry != null) {
				if (asciiKey.equals(entry.key)) {
					return true;
				} else if (asciiKey.compareTo(entry.key) < 0) {
					entry = entry.left;
				} else {
					entry = entry.right;
				}
			}
			
			return false;
		}

        /*
		 * Author: Sidney Gills
		 * Derived from CLRS Ch12.2 TREE-MINIMUM(x)
         * Returns the left-most entry of the tree, the first in alphabetical sorted order.
         */
		public Node first() {
			Node entry = root;
			while (entry.left != null) {
				entry = entry.left;
			}
			
			return entry;
		}
		
        /*
		 * Author: Sidney Gills
		 * Derived from CLRS Ch12.2 TREE-MAXIMUM(x)
         * Returns the right-most entry of the tree, the last in alphabetical sorted order.
         */
		public Node last() {
			Node entry = root;
			while (entry.right != null) {
				entry = entry.right;
			}
			
			return entry;
		}

        /*
		 * Author: Sidney Gills
		 * Derived and altered from CLRS Ch12.2 ITERATIVE-TREE-SEARCH(x,k) to accomodate no passing of pointer
		 * Helper function for successor(saying) and predecessor(saying)
         */
		public static Node search(String asciiKey) {
			Node entry = root;
			while (entry != null && !asciiKey.equals(entry.key)) {
				if (asciiKey.compareTo(entry.key) < 0) {
					entry = entry.left;
				} else {
					entry = entry.right;
				}
			}
			
			return entry;
		}
		
        /*
		 * Author: Sidney Gills
		 * Derived from a combination of CLRS Ch12.2 TREE-SUCCESSOR(x) and TREE-MAXIMUM(x) to be symmetrical as predecessor
         * Returns the node that comes directly before the one in sorted order that matches the string.
         */
		public Node predecessor(String asciiKey) {
			Node entry = search(asciiKey);
			if (entry == null) {
				return entry;
			} else if (entry.left != null) {
				entry = entry.left;
				while (entry.right != null) {
					entry = entry.right;
				}
				
				return entry;
			} else {
				Node ancestor = entry.parent;
				while (ancestor != null && entry == ancestor.left) {
					entry = ancestor;
					ancestor = ancestor.parent;
				}
				return ancestor;
			}
		}
		
        /*
		 * Author: Sidney Gills
		 * Derived from a combination of CLRS Ch12.2 TREE-SUCCESSOR(x) and TREE-MINIMUM(x)
         * Returns the node that comes directly after the one in sorted order that matches the string.
         */
		public Node successor(String asciiKey) {
			Node entry = search(asciiKey);
			if (entry == null) {
				return entry;
			} else if (entry.right != null) {
				entry = entry.right;
				while (entry.left != null) {
					entry = entry.left;
				}
				
				return entry;
			} else {
				Node ancestor = entry.parent;
				while (ancestor != null && entry == ancestor.right) {
					entry = ancestor;
					ancestor = ancestor.parent;
				}
				return ancestor;
			}
		}

        /*
         * Author: Sidney Gills
         * Prints the saying, translation, and explanation attached to the node.
         */
        public void readEntry(Node node) {
            if (node != null) {
                System.out.println("Spanish Saying: \t" + node.saying);
                System.out.println("English Translation:\t" + node.english);
                System.out.println("Explanation:\t\t" + node.explanation);
            } else {
                System.out.println("No such entry exists");
            }
        }
        
        /*
         * Author: Sidney Gills
         * Derived from CLRS Ch12.1 INORDER-TREE-WALK(x)
         * Traverses the tree in order, recursively printing the saying of each node in the order of
         * left child, parent, then right child, resulting in an alphabetically sorted list.
         */
        public void inOrderTreeWalk(Node node) {
            if (node != null) {
                inOrderTreeWalk(node.left);
                System.out.println(node.saying);
                inOrderTreeWalk(node.right);
            }
        }

        /*
         * Author: Sidney Gills
         * Prints the ArrayList data structures that hold the results of meHua and withWord.
         */
        public void printArrayList(ArrayList<String> list) {
            for (int i = 0; i < list.size(); i++) {
                System.out.println(i+1 + "\t" + list.get(i));
            }  
        }
        ArrayList<String> meHuaList = new ArrayList<>();
        ArrayList<String> withWordList = new ArrayList<>();
        
        /*
         * Author: Sidney Gills
         * Derived from CLRS Ch12.1 INORDER-TREE-WALK(x)
         * Helper function for meHua(String word)
         * Performs an in-order traversal of the tree, adding sayings that contain the passed
         * word into the ArrayList.
         */
        public void meHuaTreeWalk(Node node, String word) {
            if (node != null) {
                meHuaTreeWalk(node.left, word);
                if (hasMyWord(node.key.toLowerCase(), word)) {
                    meHuaList.add(node.saying);
                }
                meHuaTreeWalk(node.right, word);
            }
        }
        
        /*
         * Author: Sidney Gills
         * Checks that passed string is only one word, then replaces diacritics and converts to lower case.
         * Returns a list of sayings that contains the word.
         */
        public void meHua(String word) {
            meHuaList.clear();
            if (!isOneWord(word)) {
                System.out.println("MeHua | '" + word + "' | " + meHuaList.size() + " matches");
                System.out.println("'" + word + "': must be a single word and contain no spaces");
            } else {
                word = replaceDiacritics(word).toLowerCase();
                meHuaTreeWalk(root, word);
                System.out.println("MeHua | '" + word + "' | " + meHuaList.size() + " matches");
                printArrayList(meHuaList);
            }
        }
        
        /*
         * Author: Sidney Gills
         * Derived from CLRS Ch12.1 INORDER-TREE-WALK(x)
         * Helper function for withWord(String word)
         * Performs an in-order traversal of the tree, adding sayings whose translations contain
         * the passed word into the ArrayList.
         */
        public void withWordTreeWalk(Node node, String word) {
            if (node != null) {
                withWordTreeWalk(node.left, word);
                if (hasMyWord(node.english.toLowerCase(), word)) {
                    withWordList.add(node.saying);
                }
                withWordTreeWalk(node.right, word);
            }
        }
        
        /*
         * Author: Sidney Gills
         * Checks that passed string is only one word, then converts to lower case.
         * Returns a list of sayings whose translation contains the word.
         */
        public void withWord(String word) {
            withWordList.clear();
            if (!isOneWord(word)) {
                System.out.println("WithWord | '" + word + "' | " + withWordList.size() + " matches");
                System.out.println("'" + word + "': must be a single word and contain no spaces");
            } else {
                word = word.toLowerCase();
                withWordTreeWalk(root, word);
                System.out.println("WithWord | '" + word + "' | " + withWordList.size() + " matches");
                printArrayList(withWordList);
            }       
        }

        /*
         * Author: Sidney Gills
         * Derived from https://hackr.io/blog/programming-interview-questions
         * Performs a tree walk to determine the number of entries in the dictionary.
         */
        public int getCount(Node node) {            
            if (node == null) {
                return 0;
            } else {
                return 1 + getCount(node.left) + getCount(node.right);
            }
        }
        
        /*
         * Author: Sidney Gills
         * Derived from https://hackr.io/blog/programming-interview-questions
         * Performs a tree walk to determine the height of the tree.
         */
        public int getHeight(Node node) {
            if (node == null) {
                return 0;
            }
            return 1 + Math.max(getHeight(node.left), getHeight(node.right));
        }

        /*
         * Author: Sidney Gills
         * Formula to determine if tree is balanced derived from CLRS Ch13.1 pg331
         * How to calculate log base 2 derived from https://www.geeksforgeeks.org/how-to-calculate-log-base-2-of-an-integer-in-java/
         * Compares the height of the tree to the formula 2*lg(n+1) to determine if the tree is balanced.
         */
        public void isBalanced(Node node) {
            System.out.println("The dictionary tree has " + getCount(node) + " entries and a height of " + getHeight(node) + ".\n");
            if (getHeight(node) <= (2 * (Math.log(1 + getCount(node)) / Math.log(2)))) {
                System.out.println("The dictionary tree is balanced because its height does not exceed 2*lg(n+1).");
            } else {
                System.out.println("The dictionary tree is not balanced because its height exceeds 2*lg(n+1).");
            }
        }
    }

    /*
     * Author: Sidney Gills
     * Creates the tree and dictionary, and tests the program's operations and functionality.
     */
    public static void main(String[] args) {
        Dictionary dictionary = new Dictionary();

        /*
         * Author: Sidney Gills
         * Spanish sayings, English translations and explanations from https://www.spanishpod101.com/blog/2021/06/10/best-spanish-proverbs/
         */
        dictionary.insert("De tal palo, tal astilla", "From such a stick, such a chip", "Refers to characters and traits children inherit from parents.");
        dictionary.insert("Dime con quién andas, y te diré quién eres", "Tell me with whom you walk, and I’ll tell you who you are", "You can tell someone's personality by their friends or company.");
        dictionary.insert("Un clavo saca otro clavo", "One nail drives out another", "A new love interest helps one forget a heartbreak.");
        dictionary.insert("Dios los cría y ellos se juntan", "God creates them and they get together", "We tend to get together with people who resemble us in personality or interests.");
        dictionary.insert("Ojos que no ven, corazón que no siente", "Eyes that don’t see, heart that doesn’t feel", "People don’t suffer for what they don’t know.");
        dictionary.insert("Más vale pájaro en mano que cien volando", "A bird in the hand is more worthy than a hundred flying", "It’s better to make sure you keep what you have instead of taking risks that could make you lose everything.");
        dictionary.insert("A caballo regalado, no le mires el diente", "Don’t check the teeth of a gift horse", "Don't be picky about something received for free or as a gift, and to be grateful even if you don’t like it a lot.");
        dictionary.insert("A falta de pan, buenas son tortas", "In the absence of bread, cakes are good", "Emphasizes the importance of being flexible when things don’t go our way and valuing what we do have.");
        dictionary.insert("Perro ladrador, poco mordedor", "A barking dog, not much of a biter", "People who can be very threatening with their words but won’t ever act on them.");
        dictionary.insert("Aunque la mona se vista de seda, mona se queda", "Even if the monkey wears silk, it remains a monkey", "People who like to pretend they’re something they’re not.");
        dictionary.insert("En casa del herrero, cuchillo de palo", "In the house of the blacksmith, wooden knives", "Refers to people who don’t follow the advice they give or don’t lead by example.");
        dictionary.insert("Éramos pocos y parió la abuela", "We were a few and then the grandmother gave birth", "A colloquial way to say that a situation got worse.");
        dictionary.insert("Hablando del rey de Roma, que por la puerta asoma", "Speaking of the King of Rome, that comes through the door", "When someone who is being talked about appears unexpectedly, especially when that person was being criticized.");
        dictionary.insert("El mundo es un pañuelo", "The world is a handkerchief", "Commonly used as an expression of surprise when running into someone you know in a place you didn’t expect them to be.");
        dictionary.insert("Cada loco con su tema", "Each crazy person with their topic", "Refers to the different obsessions that every person has.");
        dictionary.insert("Todos los caminos llevan a Roma", "All roads lead to Rome", "Refers to the different ways in which an objective can be reached.");
        dictionary.insert("No todo el monte es orégano", "Not all the hill is oregano", "Alludes to the difficulties that are presented to us throughout the course of our lives.");
        dictionary.insert("Una golondrina no hace verano", "One swallow does not make a summer", "An isolated event is not always an indicator of what is to come.");
        dictionary.insert("Mucho ruido y pocas nueces", "Much noise and few walnuts", "Making a fuss about something that really isn’t important.");
        dictionary.insert("Dar en el clavo", "Hit the nail on the head", "When something is spot-on, like when a decision has been proven to be the right one.");
        dictionary.insert("Quien va a Sevilla, pierde su silla", "He who goes to Seville, loses its chair", "Losing privileges because of abandoning them temporarily.");
        dictionary.insert("El que ríe último, ríe mejor", "He who laughs last laughs best", "Don't declare victory before the war is over, as life can be full of surprises.");
        dictionary.insert("Tira la piedra y esconde la mano", "He throws the stone and hides the hand", "Someone who does something and doesn't take responsibility for their actions.");
        dictionary.insert("Cría fama y échate a dormir", "Raise fame and lie down to sleep", "Once created, your reputation follows you and is difficult to change.");
        dictionary.insert("El que parte y reparte, se queda la mejor parte", "He who distributes ends up with the best part", "Someone who has access to something ends up keeping the best for themselves; often used when talking about money-related corruption.");
        dictionary.insert("A quien madruga, Dios le ayuda", "He who wakes up early is helped by God", "Encourages people to wake up early so they can make the most of their day.");
        dictionary.insert("El que no llora, no mama", "He who doesn’t cry, doesn’t nurse", "When you want something, you have to ask for it.");
        dictionary.insert("Querer es poder", "To want is to be able to", "If you put in the effort to get something, you’ll get it.");
        dictionary.insert("Más vale prevenir que curar", "Better anticipate than treat", "It’s better to anticipate a bad situation before it’s too late to solve the problem.");
        dictionary.insert("Nunca digas de esta agua no beberé", "Never say ‘I won’t ever drink from that water", "As much as we hate something, we cannot ever be sure that we won’t do it at some point.");

        Dictionary.Node root = Dictionary.root;

        System.out.println("______________________________________________________________________________________");
        System.out.println("Dictionary of Spanish Sayings in Alphabetical Order:\n");
        dictionary.inOrderTreeWalk(root);
        System.out.println("\n");

        System.out.println("______________________________________________________________________________________");
        System.out.println("Counts number of entries and checks tree height to see if it is balanced:\n");
        dictionary.isBalanced(root);
        System.out.println("\n");

        System.out.println("______________________________________________________________________________________");
        System.out.println("Prints the Spanish saying, English translation, and explanation of the root entry of the dictionary:\n");
        dictionary.readEntry(root);
        System.out.println("\n");

        System.out.println("______________________________________________________________________________________");
        System.out.println("The predecessor and successor of '" + root.saying + "':\n");
        System.out.println("Predecessor:\t" + dictionary.predecessor(root.key).saying);
        System.out.println("Successor:\t" + dictionary.successor(root.key).saying);
        System.out.println("\n");

        System.out.println("______________________________________________________________________________________");
        System.out.println("The first and last sayings in the dictionary:\n");
        System.out.println("First:\t" + dictionary.first().saying);
        System.out.println("Last:\t" + dictionary.last().saying);
        System.out.println("\n");

        System.out.println("______________________________________________________________________________________");
        System.out.println("Tests the member function to see if the saying is in the dictionary:\n");
        System.out.println(dictionary.member("Mi casa es su casa") + ":\t'Mi casa es su casa' is not in the list.");
        System.out.println(dictionary.member(root.key) + ":\t'" + root.saying + "' is in the list.");
        System.out.println("\n");

        System.out.println("______________________________________________________________________________________");
        System.out.println("Testing meHua and withWord:");
        System.out.println("meHua matches if word occurs in Spanish saying; withWord matches if word occurs in English translation\n");
        dictionary.meHua("más");
        System.out.println("\n");
        dictionary.meHua("que");
        System.out.println("\n");
        dictionary.meHua("que no");
        System.out.println("\n");
        dictionary.withWord("who");
        System.out.println("\n");
        dictionary.withWord("nurse");
        System.out.println("\n");
        dictionary.withWord("rome");
        System.out.println("\n");
    }
}
 