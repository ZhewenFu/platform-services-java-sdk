/*
 * (C) Copyright IBM Corp. 2022.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.ibm.cloud.platform_services.enterprise_usage_reports.v1.model;

import com.ibm.cloud.platform_services.enterprise_usage_reports.v1.EnterpriseUsageReports;
import com.ibm.cloud.sdk.core.util.UrlHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * GetResourceUsageReportPager can be used to simplify the use of the "getResourceUsageReport" method.
 */
public class GetResourceUsageReportPager {
  private static class PageContext {
    private String next;
    public String getNext() {
      return next;
    }
    public void setNext(String next) {
      this.next = next;
    }
  }

  protected boolean hasNext;
  protected GetResourceUsageReportOptions options;
  protected EnterpriseUsageReports client;
  protected PageContext pageContext;

  // Hide the default ctor.
  protected GetResourceUsageReportPager() { }

  /**
   * Constructs a new GetResourceUsageReportPager instance with the specified client and options model instance.
   * @param client the EnterpriseUsageReports instance to be used to invoke the "getResourceUsageReport" method
   * @param options the GetResourceUsageReportOptions instance to be used to invoke the "getResourceUsageReport" method
   */
  public GetResourceUsageReportPager(EnterpriseUsageReports client, GetResourceUsageReportOptions options) {
    if (options.offset() != null) {
      throw new IllegalArgumentException("The options 'offset' field should not be set");
    }

    this.hasNext = true;
    this.client = client;
    this.options = options.newBuilder().build();
    this.pageContext = new PageContext();
  }

  /**
   * Returns true if there are more results to be retrieved.
   * @return boolean
   */
  public boolean hasNext() {
    return hasNext;
  }

  /**
   * Returns the next page of results.
   * @return a List&lt;ResourceUsageReport&gt; that contains the next page of results
   */
  public List<ResourceUsageReport> getNext() {
    if (!hasNext()) {
      throw new NoSuchElementException("No more results available");
    }

    GetResourceUsageReportOptions.Builder builder = this.options.newBuilder();
    if (this.pageContext.getNext() != null) {
      builder.offset(this.pageContext.getNext());
    }
    this.options = builder.build();

    Reports result = client.getResourceUsageReport(options).execute().getResult();

    String next = null;
    if (result.getNext() != null) {
      String queryParam = UrlHelper.getQueryParam(result.getNext().getHref(), "offset");
      if (queryParam != null) {
        next = queryParam;
      }
    }
    this.pageContext.setNext(next);
    if (next == null) {
      this.hasNext = false;
    }

    return result.getReports();
  }

  /**
   * Returns all results by invoking getNext() repeatedly until all pages of results have been retrieved.
   * @return a List&lt;ResourceUsageReport&gt; containing all results returned by the "getResourceUsageReport" method
   */
  public List<ResourceUsageReport> getAll() {
    List<ResourceUsageReport> results = new ArrayList<>();
    while (hasNext()) {
      List<ResourceUsageReport> nextPage = getNext();
      results.addAll(nextPage);
    }
    return results;
  }
}
