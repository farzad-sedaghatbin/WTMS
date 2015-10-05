package x3dom.lighting
{
	public class HeadLight extends BaseLight
	{
		/**
		 * Orientation of light relative to local coordinate system.
		 * @defaut [0.0, 0.0, -1.0]
		 */
		private var _direction:Vector.<Number> = Vector.<Number>([0.0, 0.0, -1.0]);
		
		/**
		 * 
		 */
		public function HeadLight(id:uint)
		{
			super(id);
		}
		
		/**
		 * Orientation of light relative to local coordinate system.
		 * @defaut [0.0, 0.0, -1.0]
		 */
		public function get direction() : Vector.<Number>
		{
			return _direction;
		}
		
		/**
		 * @private
		 */
		public function set direction(direction:Vector.<Number>) : void
		{
			_direction = direction;
			this._direction.push(0.0);
		}
	}
}