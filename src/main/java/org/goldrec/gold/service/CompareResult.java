package org.goldrec.gold.service;

public enum CompareResult {
	/** Относятся к одной группе */
	COINCIDENCE,
	/** Дубль */
	DUPLICATE,
	/** Не равны, другая группа*/
	NOT_EQUAL,
	/** Ошибка сравнения */
	ERROR,
	/** Дефолтное значение */
	DEF
}
