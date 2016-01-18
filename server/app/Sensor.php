 
<?php namespace App;
 
use Illuminate\Database\Eloquent\Model;
use App\User;
 
 
class Sensor extends Model {
 protected $fillable = ['sensor','value','latitude','longitude'];
 
}