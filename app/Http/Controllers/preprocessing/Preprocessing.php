<?php
namespace App\Http\Controllers\preprocessing;
use  App\Http\Controllers\preprocessing\Stemmer;
use DB;
use Storage;
use  App\Http\Controllers\posTagger\posTagger;
// preprocesing: tokenization, filtering, stopterm removal dan stemming.
// Processing
// Steaming,Stopterm, penghilangan tanda baca, pemisahan term

class Preprocessing extends Stemmer
{
	public function preprocesing($date,$start_time_tweet,$end_time_tweet){
        $terms = array();
        $clear_tweet = "";
        $pos_tagger_result ="";
        // $clear_tweets = array();
        $pos_tagger = new postagger();
    	$Stemmer = new Stemmer();

    	$tweets = DB::table('tweets')
                    ->where('date_tweet','like',$date.'%')->get();
 		foreach ($tweets as $tweet) {
            if(strtotime($tweet->date_tweet) >= strtotime($date.$start_time_tweet) && strtotime($tweet->date_tweet) <= strtotime($date.$end_time_tweet))
            {
     			foreach ($this->tokenizing($tweet->tweet) as $term)
     			{ 
                    $filtering = $this->filtering($term);
                    if($filtering)
                    {
                        if($this->stopword($filtering))
                        { 
                            //fungsi nazief untuk stemming
                            // $data_term = $this->NAZIEF($filtering);
                            $data_term = $filtering;
                            $clear_tweet .= $data_term." ";
                            if(strlen($data_term) != 0)
                            {
                                if(!array_key_exists($data_term, $terms))
                                    $terms[$data_term] = 0;
                                $terms[$data_term] = $terms[$data_term] + 1;
                            }
                        }
                    }
     			}
                // Penambahan pos tagger
                $pos_tagger->postagger(rtrim($clear_tweet,' '));
                if($pos_tagger->tweet_new){
                    DB::table('tweet_preprocessing')->insert(['id_tweet' => $tweet->id,'preprocessing' => rtrim($clear_tweet,' ')]);
                    $pos_tagger_result .= $pos_tagger->tweet_new."\n";
                }
                $clear_tweet = "";
            }
 		}
        Storage::put('public/pos_tagger_result.txt',$pos_tagger_result);
        // arsort($terms);
        return $terms;
    }
    public function filtering($term)
    {
        if((substr($term, 0,4) == 'http') || (substr($term, 0,1) == '@'))
           return false;
        return preg_replace('/[^a-z]/i','', $this->caseFolding($term));
    }

    public function caseFolding($term)
    {
    	return strtolower($term);
    }

    public function tokenizing($tweet)
    {
    	return $tokenizing = explode(' ',$tweet);
    }
    public function stopword($term)
    {
        $stop_term = DB::table('stopwords')->where('stopword',$term)->count();
        if($stop_term < 1)
            return true;
        return false;
    }

    public function posTagger() {
        $pos_tagger = new postagger();

        // Storage::put('public/tag.txt',rtrim($term,"\n"));
        // $tag = Storage::get('public/tag.txt');

        // echo "<pre>";
        // print_r(explode("\n", $tag));
        // print_r($term);
        // echo "</pre>";
        // $pos_tagger->opini($term);

        $tweet = DB::table('tweet_preprocessing')->get();
        $tweet_new;
        foreach($tweet as $key => $value) {
          $tweet_new[] = $value->preprocessing;
        }
        foreach (array_unique($tweet_new) as $key => $value) {
            if($value != ''){
                $pos_tagger->postagger($value);
                if($pos_tagger->tweet_new)
                    echo $pos_tagger->tweet_new."<br/>";
            }
        }
    }
}