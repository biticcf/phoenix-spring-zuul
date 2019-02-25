package cn.wanda.sail.common.route;

import cn.wanda.sail.tenant.admin.facade.TenantFacade;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @author: chengyang.
 * @email: chengyang14@wanda.cn
 * @time: 17/4/25
 * @description: 描述
 */
@FeignClient(name = "sail-tenant-admin", url = "http://sailtenant.intra.sit.ffan.com")
public interface TenantClient extends TenantFacade {

}