<?php namespace App;
 
use Illuminate\Database\Eloquent\Model;
use App\User;
 
 
class Sensor extends Model {
 protected $fillable = ['sensor','value','latitude','longitude','element','created_at','updated_at'];
 
}