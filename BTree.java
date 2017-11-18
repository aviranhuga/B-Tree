// SUBMIT
public class BTree implements BTreeInterface {

	// ///////////////////BEGIN DO NOT CHANGE ///////////////////
	// ///////////////////BEGIN DO NOT CHANGE ///////////////////
	// ///////////////////BEGIN DO NOT CHANGE ///////////////////
	private BNode root;
	private final int t;

	/**
	 * Construct an empty tree.
	 */
	public BTree(int t) { //
		this.t = t;
		this.root = null;
	}

	// For testing purposes.
	public BTree(int t, BNode root) {
		this.t = t;
		this.root = root;
	}

	@Override
	public BNode getRoot() {
		return root;
	}

	@Override
	public int getT() {
		return t;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((root == null) ? 0 : root.hashCode());
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
		BTree other = (BTree) obj;
		if (root == null) {
			if (other.root != null)
				return false;
		} else if (!root.equals(other.root))
			return false;
		if (t != other.t)
			return false;
		return true;
	}
	
	// ///////////////////DO NOT CHANGE END///////////////////
	// ///////////////////DO NOT CHANGE END///////////////////
	// ///////////////////DO NOT CHANGE END///////////////////

	//set a new root
    private void setRoot(BNode root) {
    	this.root=root;
    }
    
	@Override
	public Block search(int key) {
		return root.search(key);
	}

	@Override
	public void insert(Block b) {
	  if (this.root==null){
			this.setRoot(new BNode(this.t,b));
			return;
		}
	  if (this.root.isFull()){
	     setRoot(new BNode(this.t,this.root));
	     this.root.splitChild(0);
	     this.root.insertNonFull(b);
	  }else
         this.root.insertNonFull(b);
	}

	@Override
	public void delete(int key) {
			if (root.isLeaf() && root.getNumOfBlocks()==1 && key==root.getBlockAt(0).getKey()){//root is a leaf
				this.setRoot(null);
				return;
			}
			if (root.getNumOfBlocks()==1 && !(root.isLeaf()) && root.getChildAt(0).getNumOfBlocks()==(this.t-1) && root.getChildAt(1).getNumOfBlocks()==(this.t-1)){
			    this.getRoot().delete(key);
			    this.setRoot(root.getChildAt(0));
			    return;
			}
	        this.getRoot().delete(key);
	}

	@Override
	public MerkleBNode createMBT() {
		return this.root.createHashNode();
	}


}
