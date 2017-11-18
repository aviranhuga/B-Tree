import java.util.ArrayList;

//SUBMIT
public class BNode implements BNodeInterface {

	// ///////////////////BEGIN DO NOT CHANGE ///////////////////
	// ///////////////////BEGIN DO NOT CHANGE ///////////////////
	// ///////////////////BEGIN DO NOT CHANGE ///////////////////
	private final int t;
	private int numOfBlocks;
	private boolean isLeaf;
	private ArrayList<Block> blocksList;
	private ArrayList<BNode> childrenList;

	/**
	 * Constructor for creating a node with a single child.<br>
	 * Useful for creating a new root.
	 */
	public BNode(int t, BNode firstChild) {
		this(t, false, 0);
		this.childrenList.add(firstChild);
	}

	/**
	 * Constructor for creating a <b>leaf</b> node with a single block.
	 */
	public BNode(int t, Block firstBlock) {
		this(t, true, 1);
		this.blocksList.add(firstBlock);
	}

	public BNode(int t, boolean isLeaf, int numOfBlocks) {
		this.t = t;
		this.isLeaf = isLeaf;
		this.numOfBlocks = numOfBlocks;
		this.blocksList = new ArrayList<Block>();
		this.childrenList = new ArrayList<BNode>();
	}

	// For testing purposes.
	public BNode(int t, int numOfBlocks, boolean isLeaf,
			ArrayList<Block> blocksList, ArrayList<BNode> childrenList) {
		this.t = t;
		this.numOfBlocks = numOfBlocks;
		this.isLeaf = isLeaf;
		this.blocksList = blocksList;
		this.childrenList = childrenList;
	}

	@Override
	public int getT() {
		return t;
	}

	@Override
	public int getNumOfBlocks() {
		return numOfBlocks;
	}

	@Override
	public boolean isLeaf() {
		return isLeaf;
	}

	@Override
	public ArrayList<Block> getBlocksList() {
		return blocksList;
	}

	@Override
	public ArrayList<BNode> getChildrenList() {
		return childrenList;
	}

	@Override
	public boolean isFull() {
		return numOfBlocks == 2 * t - 1;
	}

	@Override
	public boolean isMinSize() {
		return numOfBlocks == t - 1;
	}
	
	@Override
	public boolean isEmpty() {
		return numOfBlocks == 0;
	}
	
	@Override
	public int getBlockKeyAt(int indx) {
		return blocksList.get(indx).getKey();
	}
	
	@Override
	public Block getBlockAt(int indx) {
		return blocksList.get(indx);
	}

	@Override
	public BNode getChildAt(int indx) {
		return childrenList.get(indx);
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((blocksList == null) ? 0 : blocksList.hashCode());
		result = prime * result
				+ ((childrenList == null) ? 0 : childrenList.hashCode());
		result = prime * result + (isLeaf ? 1231 : 1237);
		result = prime * result + numOfBlocks;
		result = prime * result + t;
		return result;
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BNode other = (BNode) obj;
		if (blocksList == null) {
			if (other.blocksList != null)
				return false;
		} else if (!blocksList.equals(other.blocksList))
			return false;
		if (childrenList == null) {
			if (other.childrenList != null)
				return false;
		} else if (!childrenList.equals(other.childrenList))
			return false;
		if (isLeaf != other.isLeaf)
			return false;
		if (numOfBlocks != other.numOfBlocks)
			return false;
		if (t != other.t)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "BNode [t=" + t + ", numOfBlocks=" + numOfBlocks + ", isLeaf="
				+ isLeaf + ", blocksList=" + blocksList + ", childrenList="
				+ childrenList + "]";
	}

	// ///////////////////DO NOT CHANGE END///////////////////
	// ///////////////////DO NOT CHANGE END///////////////////
	// ///////////////////DO NOT CHANGE END///////////////////
	
	private void setNumOfBlocks(int numOfBlocks) {
		this.numOfBlocks=numOfBlocks;
	}
	
	public void setisLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	
	@Override
	public Block search(int key) {
		int i=0;
		while (i < this.numOfBlocks && key > this.blocksList.get(i).getKey())
			i++;
		if (i < this.numOfBlocks && key==this.blocksList.get(i).getKey())
			return this.blocksList.get(i);
		else if (this.isLeaf)// if x is leaf we didnt found
		    return null;
		return this.childrenList.get(i).search(key); // search in the i children
	}

	@Override
	public void insertNonFull(Block d) {
	   int i = this.numOfBlocks;
	   if (this.isLeaf){
		  while( i>0 && d.getKey() < this.blocksList.get(i-1).getKey()){
			  i--;
		  }//end of while
		  this.blocksList.add(i,d);
		  this.numOfBlocks++;
	   }//end of if
	   else{//not a leaf
		   while( i>0 && d.getKey() < this.blocksList.get(i-1).getKey()){
			  i--;
	       }//end of while
		   //i++;
		   if (this.childrenList.get(i).isFull()){
			   splitChild(i);
		       if ( d.getKey() > this.blocksList.get(i).getKey() )
		    	   i++;
		   }//end of if
		this.childrenList.get(i).insertNonFull(d);
	   }//end of else
	}

	@Override
	public void delete(int key) {
		if(this.search(key)!=null){// we have the key in tree
		  int i=0;
		  while (i < this.numOfBlocks && key > this.blocksList.get(i).getKey())
			i++;
		  if (i < this.numOfBlocks && key==this.blocksList.get(i).getKey()){//found the key
			  if (this.isLeaf){//CASE 1 we are in a leaf
				  this.getBlocksList().remove(i);
				  this.setNumOfBlocks(this.getNumOfBlocks()-1);
				  return;
			      }//end of case1
			  if (i>=0 && this.getChildrenList().get(i).getNumOfBlocks() >= this.t){ //CASE 2
				  //save the predecessor
				  Block predecessor = this.getChildrenList().get(i).getMaxKeyBlock();
				  //delete the predecessor
				  this.getChildrenList().get(i).delete(predecessor.getKey());
				  // replace with the predecessor
				  this.getBlocksList().set(i,predecessor);
		          return;
			      }//end of case2
		      if ( i<=this.getNumOfBlocks() && this.getChildrenList().get(i+1).getNumOfBlocks() >= this.t){ //CASE 3
				  //save the successor
				  Block successor = this.getChildrenList().get(i+1).getMinKeyBlock();
				  //delete the predecessor
				  this.getChildrenList().get(i+1).delete(successor.getKey());
				  // replace with the predecessor
				  this.getBlocksList().set(i,successor);
		          return;
		          }//end of case 3
			   //WE IN CASE 4
		       this.mergeWithRightSibling(i);
			   this.getChildrenList().get(i).delete(key);
		  }//end of key found
		  
		  this.shiftOrMergeChildIfNeeded(i);
          if (i>=0 && i<=this.getNumOfBlocks() && this.getChildAt(i).search(key)!=null)//key is in i
		      this.getChildrenList().get(i).delete(key);
          else if (i>0 && this.getChildAt(i-1).search(key)!=null) // key is in i-1
        	  this.getChildrenList().get(i-1).delete(key);
          else if ( i<this.getNumOfBlocks() && this.getChildAt(i+1).search(key)!=null)// key is in i+1
        	  this.getChildrenList().get(i+1).delete(key);
		}//end of if
	}

	@Override
	public MerkleBNode createHashNode() {
		ArrayList<byte[]> dataList = new ArrayList<byte[]>();
		ArrayList<MerkleBNode> children = new ArrayList<MerkleBNode>();
		//HashUtils hash = new HashUtils();
		if (this.isLeaf){
		   for(int i=0 ; i<this.getNumOfBlocks() ; i++)
			   dataList.add(this.blocksList.get(i).getData());
		   return new MerkleBNode(HashUtils.sha1Hash(dataList));
		}//NOT a leaf
		
		//children list
	    for(int i=0 ; i<=this.getNumOfBlocks() ; i++)
	    	children.add(this.getChildAt(i).createHashNode());
	    //create dataList
	    for(int i=0 ; i<=this.getNumOfBlocks() ; i++){
		   dataList.add(children.get(i).getHashValue());
		   if (i<this.getNumOfBlocks())
		   dataList.add(this.blocksList.get(i).getData());
	    }//end of dataList
	    return new MerkleBNode(HashUtils.sha1Hash(dataList),children);
	}
	
	/**
	* Splits the child node at childIndex into 2 nodes.
	* @param childIndex
	*/
	public void splitChild(int childIndex){
		
	  BNode y = this.childrenList.get(childIndex);
	  BNode z = new BNode(this.t,y.isLeaf(),this.t-1);
	  
	  for (int j=0 ; j < (this.t-1) ; j++)// move blocks
		 z.getBlocksList().add(j,y.getBlocksList().remove(this.t));
	  if (!(y.isLeaf)){// move children
		 for (int i=0 ; i < this.t ; i++)
			z.getChildrenList().add(i,y.childrenList.remove(this.t));	 
	  }//end of if
	  this.childrenList.add(childIndex+1,z);

	  this.getBlocksList().add(childIndex,y.getBlocksList().remove(this.t-1));

	  //fix number of blocks
	  this.numOfBlocks++;
	  y.setNumOfBlocks(this.t-1);
	}
	/**
	* True iff the child node at childIndx-1 exists and has more than t-1 blocks.
	* @param childIndx
	* @return
	*/
	// assuming we call from X
	private boolean childHasNonMinimalLeftSibling(int childIndx){
		boolean answer=false;
		if (childIndx>0){ // there is a node at childIndx-1
		answer = (this.getChildrenList().get(childIndx-1).getNumOfBlocks() > (this.t-1));
		}
		return answer;
	}
	/**
	* True iff the child node at childIndx+1 exists and has more than t-1 blocks.
	* @param childIndx
	* @return
	*/
	private boolean childHasNonMinimalRightSibling(int childIndx){
		boolean answer=false;
		if ( childIndx < this.numOfBlocks){ // there is a node at childIndx+1
		answer = (this.getChildrenList().get(childIndx+1).getNumOfBlocks() > (this.t-1));
		}
		return answer;
	}
	
	/**
	* Verifies the child node at childIndx has at least t blocks.<br>
	* If necessary a shift or merge is performed.
	*
	* @param childIndxxs
	*/
	private void shiftOrMergeChildIfNeeded(int childIndx){
		if (this.getChildrenList().get(childIndx).getNumOfBlocks()>=this.t)
			return;
		else{// hes got t-1 blocks
			if (this.childHasNonMinimalLeftSibling(childIndx))
				this.shiftFromLeftSibling(childIndx);
			else if (this.childHasNonMinimalRightSibling(childIndx))
				this.shiftFromRightSibling(childIndx);
			else this.mergeChildWithSibling(childIndx);
			}
	}
	/**
	* Add additional block to the child node at childIndx, by shifting from left sibling.
	* @param childIndx
	*/
	private void shiftFromLeftSibling(int childIndx){
		BNode lastchild=null;
	  //left sibling changes
	  //delete and save the max block
	   Block maxblock = this.getChildrenList().get(childIndx-1).getBlocksList().remove(this.getChildrenList().get(childIndx-1).getNumOfBlocks()-1);
	  //delete and save the biggest child
	  if (!(this.getChildrenList().get(childIndx-1).isLeaf))
	  lastchild = this.getChildrenList().get(childIndx-1).getChildrenList().remove(this.getChildrenList().get(childIndx-1).getNumOfBlocks());
	  //number of blocks -1
	  this.getChildrenList().get(childIndx-1).setNumOfBlocks(this.getChildrenList().get(childIndx-1).getNumOfBlocks()-1);
	  
	  //on this Node
	  // change and save the biggest block with the separate block
	  Block sepBlock = this.getBlocksList().set(childIndx-1, maxblock);
	  
	  //insert the last child child
	  if (!(this.getChildrenList().get(childIndx-1).isLeaf))
	  this.getChildAt(childIndx).getChildrenList().add(0,lastchild);
	//insert the separate block
	  this.getChildAt(childIndx).getBlocksList().add(0, sepBlock);
	  //number of blocks +1
	  this.getChildAt(childIndx).setNumOfBlocks(this.getChildAt(childIndx).getNumOfBlocks()+1);
	}
	
	/**
	* Add additional block to the child node at childIndx, by shifting from right sibling.
	* @param childIndx
	*/
	private void shiftFromRightSibling(int childIndx){
	  BNode firstchild = null;
	  //right sibling changes
	  //delete and save the min block
	  Block minblock = this.getChildrenList().get(childIndx+1).getBlocksList().remove(0);
	  //delete and save the first child if no a leaf
	  if (!(this.getChildrenList().get(childIndx+1).isLeaf))
	  firstchild = this.getChildrenList().get(childIndx+1).getChildrenList().remove(0);
	  //number of blocks -1
	  this.getChildrenList().get(childIndx+1).setNumOfBlocks(this.getChildrenList().get(childIndx+1).getNumOfBlocks()-1);
	  
	  //on this Node
	  // change and save the biggest block with the separate block
	  Block sepBlock = this.getBlocksList().set(childIndx, minblock);
	  
	  //insert the first child
	  if (!(this.getChildrenList().get(childIndx+1).isLeaf))
	  this.getChildAt(childIndx).getChildrenList().add(firstchild);
	//insert the separate block
	  this.getChildAt(childIndx).getBlocksList().add(sepBlock);
	  //number of blocks +1
	  this.getChildAt(childIndx).setNumOfBlocks(this.getChildAt(childIndx).getNumOfBlocks()+1);
	}
		
	/**
	* Merges the child node at childIndx with its left or right sibling.
	* @param childIndx
	*/
	private void mergeChildWithSibling(int childIndx){
		if (childIndx==0)//no left sibling
			this.mergeWithRightSibling(childIndx);
		else
			this.mergeWithLeftSibling(childIndx);
	}
	/**
	* Merges the child node at childIndx with its left sibling.<br>
	* The left sibling node is removed.
	* @param childIndx
	*/
	private void mergeWithLeftSibling(int childIndx){
		//delete and save the separate block
		Block Sepblock = this.getBlocksList().remove(childIndx-1);
		//add the separate block to the left sibling
		this.getChildrenList().get(childIndx-1).getBlocksList().add(Sepblock);
	    //add all the blocks and child to the left sibling
		ArrayList<Block> blocks = this.getChildrenList().get(childIndx).getBlocksList();
		while (!blocks.isEmpty())
			this.getChildrenList().get(childIndx-1).getBlocksList().add(blocks.remove(0));
		ArrayList<BNode> nodes = this.getChildrenList().get(childIndx).getChildrenList();
		while (!nodes.isEmpty())
			this.getChildrenList().get(childIndx-1).getChildrenList().add(nodes.remove(0));
		//remove the child in childIndex
		this.getChildrenList().remove(childIndx);
		//update the number of blocks
		this.getChildrenList().get(childIndx-1).setNumOfBlocks(2*(this.t)-1);
		this.setNumOfBlocks(this.getNumOfBlocks()-1);
	}
	/**
	* Merges the child node at childIndx with its right sibling.<br>
	* The right sibling node is removed.
	* @param childIndx
	*/
	private void mergeWithRightSibling(int childIndx){
		//delete and save the separate block
		Block Sepblock = this.getBlocksList().remove(childIndx);
		//add the separate block to the right sibling
		this.getChildrenList().get(childIndx+1).getBlocksList().add(0,Sepblock);;
	    //add all the blocks and child of the sibling
		ArrayList<Block> blocks = this.getChildrenList().get(childIndx+1).getBlocksList();
		while (!blocks.isEmpty())
			this.getChildrenList().get(childIndx).getBlocksList().add(blocks.remove(0));
		ArrayList<BNode> nodes = this.getChildrenList().get(childIndx+1).getChildrenList();
		while (!nodes.isEmpty())
			this.getChildrenList().get(childIndx).getChildrenList().add(nodes.remove(0));
		//remove the child in childIndex
		this.getChildrenList().remove(childIndx+1);
		//update the number of blocks
		this.getChildrenList().get(childIndx).setNumOfBlocks(2*(this.t)-1);
		this.setNumOfBlocks(this.getNumOfBlocks()-1);
	}
	/**
	* Finds and returns the block with the min key in the subtree.
	* @return min key block
	*/
	private Block getMinKeyBlock(){
		if (!this.isLeaf)
			 return this.getChildrenList().get(0).getMinKeyBlock();
		else return this.getBlockAt(0);
	}
	/**
	* Finds and returns the block with the max key in the subtree.
	* @return max key block
	*/
	private Block getMaxKeyBlock(){
		if (!this.isLeaf)
			 return this.getChildrenList().get(this.getNumOfBlocks()).getMaxKeyBlock();
		else return this.getBlockAt(numOfBlocks-1);
	}
	

}
