/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.reporting.data.encounter.evaluator;

import org.openmrs.Encounter;
import org.openmrs.module.reporting.data.encounter.EvaluatedEncounterData;
import org.openmrs.module.reporting.data.encounter.definition.EncounterDataDefinition;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.HqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Evaluates a EncounterDatetimeDataDefinition to produce a EncounterData
 */
public abstract class EncounterPropertyDataEvaluator implements EncounterDataEvaluator {

	@Autowired
	private EvaluationService evaluationService;

	public abstract String getPropertyName();

	/**
	 * @see org.openmrs.module.reporting.data.encounter.evaluator.EncounterDataEvaluator#evaluate(org.openmrs.module.reporting.data.encounter.definition.EncounterDataDefinition, org.openmrs.module.reporting.evaluation.EvaluationContext)
	 * @should return all encounter datetimes given the passed context
	 */
	public EvaluatedEncounterData evaluate(EncounterDataDefinition definition, EvaluationContext context) throws EvaluationException {
		EvaluatedEncounterData c = new EvaluatedEncounterData(definition, context);
		HqlQueryBuilder q = new HqlQueryBuilder();
		q.select("e.encounterId", "e."+getPropertyName());
		q.from(Encounter.class, "e");
		q.whereEncounterIn("e.encounterId", context);
		Map<Integer, Object> data = evaluationService.evaluateToMap(q, Integer.class, Object.class, context);
		c.setData(data);
		return c;
	}
}
