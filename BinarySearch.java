private static int binarySearch(ArrayList<Song> list, int firstElem, int lastElem, Song input)
	 {       
	     int middle=0;
	     
	     if (list.size() == 0 || firstElem > lastElem) {
	    	 return 0;
	     }

	     middle = (firstElem + lastElem) / 2;

	     if(list.get(middle).getName().equals(input.getName()) && list.get(middle).getArtist().equals(input.getArtist())){
	         return -1;
	     }else if(list.get(middle).getName().compareToIgnoreCase(input.getName()) > 0){
	         return binarySearch(list,middle+1,lastElem, input);
	     }
	     else {
	         return binarySearch(list, firstElem, middle -1, input);            
	     }
	 }
