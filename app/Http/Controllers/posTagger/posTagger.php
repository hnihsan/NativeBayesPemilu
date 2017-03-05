<?php
namespace App\Http\Controllers\posTagger;
use DB;
use Storage;
class posTagger {

	public $tweet_new;
    
    public function posTagger($tweets){
      $tweet_new = '';
      $tagset = '';
      $ruleOpini = $this->ruleOpini();

      $data = Storage::get('public/inlex.txt');
      $data = explode("\n", $data);

      foreach (explode(' ',$tweets) as $key_tweet => $value_tweet) {
        foreach ($data as $key => $value) {
          $r  = explode("\t",$value);
          if($value_tweet == $r[0]){
              $h = explode(' ',$r[1]);
              $tweet_new .= $h[1].''.$h[0].' ';
              $tagset .= $h[0].' ';
              break;
          }
        }
      }

      if($tweet_new != ''){
			foreach ($ruleOpini as $key => $value) {
				if(strpos(rtrim($tagset,' '),$value) !== false){
				    // echo 'Rule opini: '.rtrim($value)."<br/>";
				    // echo 'Tagset: '.rtrim($tagset,' ')."<br/>";
				    // echo $tweet_new.'('.$tweets.")<br/>";
				    $this->tweet_new = $tweet_new;
				    break;
				}else{
					$this->tweet_new = '';
		      	}
			}
      	}else{
			$this->tweet_new = '';
      	}
    }

    public function ruleOpini() {
    	$ruleOpini = [	
    					'RB JJ' ,
				    	'RB VB' ,
				    	'NN JJ' ,
				    	'NN VB' ,
				    	'JJ VB' ,
				    	'CK JJ' ,
				    	'JJ BB' ,
				    	'VB VB' ,
				    	'JJ RB' ,
				    	'VB JJ' ,
				    	'NEG JJ' ,
				    	'NEG VB' ,
				    	'PRP VBI' ,
				    	'PRP VBT' ,
				    	'VBT NN' ,
				    	'D VBT' ,
				    	'MD VBI'
				    ];
		return $ruleOpini;
    }
}