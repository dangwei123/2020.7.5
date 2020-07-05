class Node<E extends Comparable<E>>{
    E key;
    int bf;
    Node left;
    Node right;
    Node parent;

    public Node(E key,Node parent){
        this.key=key;
        this.parent=parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<?> node = (Node<?>) o;
        return key.equals(node.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}

/**
 * 定义AVL树，实现简单的插入功能
 */
class AVLTree<E extends Comparable<E>>{

    //根节点
    public Node root;

    /**
     * 插入
     * @param :要插入的值
     */
    public void insert(E key){

        //如果为根节点为空，直接插入成功返回
        if(null==root){
            root=new Node(key,null);
            return;
        }

        //找到要插入的位置
        Node parent=null;
        Node cur=root;
        while(null!=cur){
            if(key.equals(parent.key)){
                //不能有重复的元素
                throw new RuntimeException("有重复元素，插入失败");
            }else if(key.compareTo((E)parent.key)<0){
                parent=cur;
                cur=cur.left;
            }else{
                parent=cur;
                cur=cur.right;
            }
        }

        //插入
        cur=new Node(key,parent);
        if(key.compareTo((E)parent.key)<0){
            parent.left=cur;
        }else{
            parent.right=cur;
        }

        /**
         * 恢复调整
         * 计算平衡因子的算法采用的是节点的左子树高度减右子树的高度
         * cur为插入的新节点
         * parent为插入节点的双亲
         */
        while(true){

            //重新计算parent的平衡因子，如果在左子树中插入的就加1，如果在右子树中插入的就减1
            if(parent.left==cur){
                parent.bf++;
            }else{
                parent.bf--;
            }

            /**
             * parent.bf==0   --->
             *
             */
            if(parent.bf==0){
                //parent的平衡因子为0，调整成功，可以直接退出
                break;
            }else if(parent.bf==2){
                //左子树比右子树高2，需要调整
                //如果cur.bf为1，是左右不平衡
                //如果cur.bf为-1，是左右不平衡
                if(cur.bf==1){
                    fixleftLeftBalance(parent);
                }else{
                    fixleftRightBalance(parent);
                }
                break;
            }else if(parent.bf==-2){
                //右子树比左子树高2，需要调整
                //如果cur.bf为-1，是右右不平衡
                //如果cur.bf为1，是右左不平衡
                if(cur.bf==-1){
                    fixrightRightBalance(parent);
                }else{
                    fixrightLeftBalance(parent);
                }
                break;
            }else if(parent==root){
                //调整到根节点，可以退出
                break;
            }

            //parent的平衡因子并没有被破坏，但是parent的高度改变，需要向上蔓延检查
            cur=parent;
            parent=parent.parent;
        }
    }

    /**
     * 左旋转
     * @param parent
     */
    private void leftRemote(Node parent){
        Node right=parent.right;
        Node leftOfRight=right.left;
        Node grandParent=parent.parent;

        right.parent=grandParent;
        if(grandParent==null){
            root=right;
        }else if(parent==grandParent.left){
            grandParent.left=right;
        }else{
            grandParent.right=right;
        }

        right.left=parent;
        parent.parent=right;

        parent.right=leftOfRight;
        if(leftOfRight!=null){
            leftOfRight.parent=parent;
        }
    }

    /**
     * 右旋转
     * @param parent
     */
    private void rightRemote(Node parent){
        Node left=parent.left;
        Node rightOfLeft=left.right;
        Node grandParent=parent.parent;

        left.parent=grandParent;
        if(grandParent==null){
            root=left;
        }else if(parent==grandParent.left){
            grandParent.left=left;
        }else{
            grandParent.right=left;
        }

        left.right=parent;
        parent.parent=left;

        parent.left=rightOfLeft;
        if(rightOfLeft!=null){
            rightOfLeft.parent=parent;
        }
    }

    /**
     * 右左失衡修复
     * @param parent
     */
    private void fixrightLeftBalance(Node parent) {
        Node right=parent.right;
        Node leftOfRight=right.left;
        rightRemote(right);;
        leftRemote(parent);

        if(leftOfRight.bf==1){
            right.bf=-1;
            right.bf=leftOfRight.bf=0;
        }else if(leftOfRight.bf==-1){
            parent.bf=1;
            right.bf=leftOfRight.bf=0;
        }else{
            leftOfRight.bf=parent.bf=right.bf=0;
        }
    }

    /**
     * 右右失衡修复
     * @param parent
     */
    private void fixrightRightBalance(Node parent) {
        Node cur=parent.right;
        rightRemote(parent);
        parent.bf=cur.bf=0;
    }

    /**
     * 左右失衡修复
     * @param parent
     */
    private void fixleftRightBalance(Node parent) {
        Node left=parent.left;
        Node rightOfLeft=left.right;
        leftRemote(left);
        rightRemote(parent);

        if(rightOfLeft.bf==1){
            parent.bf=-1;
            left.bf=rightOfLeft.bf=0;
        }else if(rightOfLeft.bf==-1){
            left.bf=1;
            parent.bf=rightOfLeft.bf=0;
        }else{
            left.bf=parent.bf=rightOfLeft.bf=0;
        }
    }

    /**
     * 左左失衡修复
     * @param parent
     */
    private void fixleftLeftBalance(Node parent) {
        Node cur=parent.left;
        rightRemote(parent);
        parent.bf=cur.bf=0;
    }

    /**
     * 是否包含某个元素
     * @param key
     */
    public boolean contains(E key){
        Node cur=root;
        while(null!=cur){
            if(key==cur.key){
                return true;
            }else if(key.compareTo((E)cur.key)<0){
                cur=cur.left;
            }else{
                cur=cur.right;
            }
        }

        return false;
    }
}