/*
/* Copyright 2018-2023 contributors to the OpenLineage project
/* SPDX-License-Identifier: Apache-2.0
*/

package io.openlineage.spark.agent.lifecycle.plan;

import io.openlineage.client.OpenLineage.InputDataset;
import io.openlineage.spark.agent.lifecycle.Rdds;
import io.openlineage.spark.api.DatasetFactory;
import io.openlineage.spark.api.OpenLineageContext;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan;
import org.apache.spark.sql.execution.ExternalRDD;

/** {@link RDD} node visitor for {@link ExternalRDD}s. */
@Slf4j
public class ExternalRDDVisitor extends AbstractRDDNodeVisitor<ExternalRDD<?>, InputDataset> {

  public ExternalRDDVisitor(OpenLineageContext context) {
    super(context, DatasetFactory.input(context));
  }

  @Override
  public List<InputDataset> apply(LogicalPlan x) {
    ExternalRDD externalRDD = (ExternalRDD) x;
    List<RDD<?>> fileRdds = Rdds.findFileLikeRdds(externalRDD.rdd());
    log.info("LogicalPlan fileRdds {}", fileRdds.toString());
    return findInputDatasets(fileRdds, externalRDD.schema());
  }
}
