class test{
    public static void main(String[] args){
        test ba = new test();
        String aq = ba.iteroloop();
        System.out.println(aq);
    }
    public String iteroloop(){
        String abc = "";
        for(int i=0;i<5;i++){
            abc += 1;
        }
        return abc;
    }
}

