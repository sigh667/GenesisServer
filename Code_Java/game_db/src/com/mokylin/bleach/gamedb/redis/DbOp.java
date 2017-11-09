package com.mokylin.bleach.gamedb.redis;

/**
 * 脏数据的操作类型
 * @author baoliang.shen
 *
 */
public enum DbOp {
	/**更新*/
	UPDATE {
		@Override
		public DbOp merge(DbOp curOperateType) {
			switch (curOperateType) {
			case UPDATE:
				return UPDATE;
			case DELETE:
				return DELETE;
			default:
				throw new RuntimeException("unknown DbOp:"+curOperateType);
			}
		}
	},
	/**删除*/
	DELETE {
		@Override
		public DbOp merge(DbOp curOperateType) {
			switch (curOperateType) {
			case UPDATE:
				throw new RuntimeException("DbOp can not from DELETE to "+curOperateType);
			case DELETE:
				return DELETE;
			default:
				throw new RuntimeException("unknown DbOp:"+curOperateType);
			}
		}
	},
	;
	
	public abstract DbOp merge(DbOp curOperateType);
}
